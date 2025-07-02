pipeline {
    agent any

    stages {
        stage('Build Spring Boot') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Build Docker image') {
            steps {
                sh 'docker build -t homelab-test:latest .'
            }
        }
        stage('Load image to k3s/containerd') {
            steps {
                sh 'docker save homelab-test:latest | ctr -n k8s.io images import -'
            }
        }
        stage('Helm deploy') {
            steps {
                sh 'helm upgrade --install homelab-test ./charts --set image.repository=homelab-test --set image.tag=latest --set image.pullPolicy=Never --namespace homelab --create-namespace'
            }
        }
    }
}
