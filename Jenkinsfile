pipeline {
    agent {
        kubernetes {
            label 'maven-agent'
            defaultContainer 'maven'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
    - name: maven
      image: maven:3.9.6-eclipse-temurin-21
      command:
      - cat
      tty: true
    - name: dind
      image: docker:25.0.3-dind
      securityContext:
        privileged: true
      tty: true
    - name: helm
      image: alpine/helm:3.14.4
      command:
      - cat
      tty: true
    - name: ctr
      image: ghcr.io/containerd/ctr:latest
      command:
      - cat
      tty: true
"""
        }
    }
    stages {
        stage('Build Spring Boot') {
            steps {
                container('maven') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        stage('Build Docker image') {
            steps {
                container('dind') {
                    sh 'docker build -t homelab-test:latest .'
                }
            }
        }
        stage('Load image to containerd') {
            steps {
                container('ctr') {
                    sh 'docker save homelab-test:latest | ctr -n k8s.io images import -'
                }
            }
        }
        stage('Helm deploy') {
            steps {
                container('helm') {
                    sh 'helm upgrade --install homelab-test ./charts --set image.repository=homelab-test --set image.tag=latest --set image.pullPolicy=Never --namespace homelab --create-namespace'
                }
            }
        }
    }
}
