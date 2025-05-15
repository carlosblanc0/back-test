pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'agents-of-revature-backend'
        DOCKER_TAG = "${BUILD_NUMBER}"
        DB_NAME = 'jenkinsdb'
        DB_USER = 'jenkins_admin'
        DB_PASSWORD = 'your_secure_password_here'
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
                    # Create network if it doesn't exist
                    docker network create ${NETWORK_NAME} || true

                    # Run PostgreSQL container
                    docker run -d \
                        --name postgres \
                        --network ${NETWORK_NAME} \
                        -e POSTGRES_DB=${DB_NAME} \
                        -e POSTGRES_USER=${DB_USER} \
                        -e POSTGRES_PASSWORD=${DB_PASSWORD} \
                        -p 5432:5432 \
                        postgres:14.18

                    # Wait for PostgreSQL to be ready
                    echo "Waiting for PostgreSQL to be ready..."
                    sleep 10

                    # Run the application container
                    docker run -d \
                        --name app \
                        --network ${NETWORK_NAME} \
                        -p ${BACKEND_PORT}:8080 \
                        -e SPRING_PROFILES_ACTIVE=prod \
                        -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/${DB_NAME} \
                        -e SPRING_DATASOURCE_USERNAME=${DB_USER} \
                        -e SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD} \
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
                        docker logs --tail 50 app
                        
                        # Extract the generated password from logs
                        GENERATED_PASSWORD=$(docker logs app | grep "Using generated security password:" | tail -n 1 | awk '{print $NF}')
                        
                        # Try to connect to the application with the generated password
                        if curl -s -f -u "user:${GENERATED_PASSWORD}" http://localhost:${BACKEND_PORT}/actuator/health > /dev/null; then
                            echo "Backend is ready!"
                            break
                        fi
                        
                        if [ $i -eq 30 ]; then
                            echo "Backend failed to start within timeout. Container logs:"
                            docker logs app
                            exit 1
                        fi
                        
                        echo "Backend not ready yet, waiting..."
                        sleep 10
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