pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'agents-of-revature'
        DOCKER_TAG = "${BUILD_NUMBER}"
        DB_NAME = 'jenkinsdb'
        DB_USER = 'jenkins_admin'
        DB_PASSWORD = 'your_secure_password_here'
        NETWORK_NAME = 'app-network'
    }

    stages {
        stage('Build') {
            steps {
                sh 'echo "Building the application..."'
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
                    sleep 10

                    # Run the application container
                    docker run -d \
                        --name app \
                        --network ${NETWORK_NAME} \
                        -p 8081:8080 \
                        -e SPRING_PROFILES_ACTIVE=prod \
                        -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/${DB_NAME} \
                        -e SPRING_DATASOURCE_USERNAME=${DB_USER} \
                        -e SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD} \
                        ${DOCKER_IMAGE}:${DOCKER_TAG}
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            echo 'Cleaning up...'
            sh '''
                docker stop app postgres || true
                docker rm app postgres || true
                docker network rm ${NETWORK_NAME} || true
                docker system prune -f
            '''
        }
    }
}