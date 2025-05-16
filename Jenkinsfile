pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'agents-of-revature-backend'
        DOCKER_TAG = "${BUILD_NUMBER}"
        DB_NAME = 'jenkinsdb'
        SPRING_DATASOURCE_URL = 'jdbc:postgresql://postgres:5432/jenkinsdb'
        SPRING_DATASOURCE_USERNAME = 'jenkins_admin'
        SPRING_DATASOURCE_PASSWORD = 'your_secure_password_here'
        NETWORK_NAME = 'app-network'
        BACKEND_PORT = '8081'
        CORS_ALLOWED_ORIGINS = 'http://localhost:8082,http://jenkins-ec2:8082,http://54.167.50.190:8080,http://54.167.50.190:8082'
    }

    stages {
        stage('Build') {
            steps {
                sh 'echo "Building the application..."'
                sh 'chmod +x ./mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'echo "Running tests..."'
                sh './mvnw test'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'echo "Building Docker image..."'
                sh 'docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} -f Dockerfile .'
            }
        }

        stage('Deploy') {
            steps {
                sh 'echo "Deploying the application..."'
                sh '''
                    # Clean up existing containers and network
                    echo "Cleaning up existing containers and network..."
                    docker stop postgres app || true
                    docker rm postgres app || true
                    docker network rm ${NETWORK_NAME} || true
                    
                    # Create network
                    echo "Creating network..."
                    docker network create ${NETWORK_NAME}
                    
                    # Run PostgreSQL container
                    echo "Starting PostgreSQL..."
                    docker run -d \
                        --name postgres \
                        --network ${NETWORK_NAME} \
                        -e POSTGRES_DB=${DB_NAME} \
                        -e POSTGRES_USER=${SPRING_DATASOURCE_USERNAME} \
                        -e POSTGRES_PASSWORD=${SPRING_DATASOURCE_PASSWORD} \
                        -p 5432:5432 \
                        postgres:14.18

                    # Wait for PostgreSQL to be ready
                    echo "Waiting for PostgreSQL to be ready..."
                    sleep 10

                    # Run the application container
                    echo "Starting application..."
                    docker run -d \
                        --name app \
                        --network ${NETWORK_NAME} \
                        -p ${BACKEND_PORT}:8081 \
                        -e SPRING_PROFILES_ACTIVE=prod \
                        -e SERVER_PORT=8081 \
                        -e SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL} \
                        -e SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME} \
                        -e SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD} \
                        -e CORS_ALLOWED_ORIGINS=${CORS_ALLOWED_ORIGINS} \
                        ${DOCKER_IMAGE}:${DOCKER_TAG}

                    # Give the application some time to start
                    echo "Waiting for application to initialize..."
                    sleep 30

                    # Wait for backend to be ready
                    echo "Waiting for backend to be ready..."
                    for i in $(seq 1 30); do
                        echo "Attempt $i: Checking backend health..."
                        # Check if container is running
                        if ! docker ps | grep -q app; then
                            echo "Backend container is not running. Checking logs:"
                            docker logs app
                            exit 1
                        fi
                        
                        # Show container logs for debugging
                        echo "Container logs:"
                        docker logs --tail 20 app
                        
                        # Try to connect to the application using the signup endpoint
                        if curl -s -f -X POST \
                            -H "Content-Type: application/json" \
                            -d '{"username":"testuser@mail","password":"T@est123!"}' \
                            http://localhost:${BACKEND_PORT}/auth/signup > /dev/null; then
                            echo "Backend is ready!"
                            break
                        fi
                        
                        if [ $i -eq 30 ]; then
                            echo "Backend failed to start within timeout. Container logs:"
                            docker logs app
                            exit 1
                        fi
                        
                        echo "Backend not ready yet, waiting..."
                        sleep 15
                    done
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully! Containers are running and accessible.'
            echo 'Backend is available at: http://54.167.50.190:8081'
            echo 'PostgreSQL is available at: 54.167.50.190:5432'
        }
        failure {
            echo 'Pipeline failed! Cleaning up...'
            sh '''
                docker stop app postgres || true
                docker rm app postgres || true
                docker network rm ${NETWORK_NAME} || true
                docker system prune -f
            '''
        }
        always {
            sh 'docker system prune -f'
        }
    }
}