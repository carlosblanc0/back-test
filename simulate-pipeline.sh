#!/bin/bash

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check for required tools
echo "🔍 Checking requirements..."
if ! command_exists docker; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

if ! command_exists docker-compose; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo "🚀 Starting pipeline simulation..."

# Stage 1: Build
echo "🔨 Stage 1: Building application..."
if ! ./mvnw clean package -DskipTests; then
    echo "❌ Build failed!"
    exit 1
fi

# Stage 2: Test
echo "🧪 Stage 2: Running tests..."
if ! ./mvnw test; then
    echo "❌ Tests failed!"
    exit 1
fi

# Stage 3: Build Docker Image
echo "🐳 Stage 3: Building Docker image..."
if ! docker build -t agents-of-revature:local -f ci/Dockerfile .; then
    echo "❌ Docker build failed!"
    exit 1
fi

# Stage 4: Deploy
echo "🚀 Stage 4: Deploying application..."
cd ci
if ! docker-compose up -d; then
    echo "❌ Deployment failed!"
    exit 1
fi

echo "✅ Pipeline completed!"
echo "🌐 Application should be available at http://localhost:8081"
echo "📊 Database is available at localhost:5432"

# Show running containers
echo "📦 Running containers:"
docker-compose ps 