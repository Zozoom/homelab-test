pipeline {
    agent {
        kubernetes {
            label 'homelab-agent'
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
        runAsUser: 0
      volumeMounts:
        - name: containerd-sock
          mountPath: /run/containerd/containerd.sock
        - name: containerd-lib
          mountPath: /var/lib/rancher/k3s/agent/containerd
      tty: true
    - name: helm
      image: alpine/helm:3.14.4
      command:
      - cat
      tty: true
  volumes:
    - name: containerd-sock
      hostPath:
        path: /run/containerd/containerd.sock
        type: Socket
    - name: containerd-lib
      hostPath:
        path: /var/lib/rancher/k3s/agent/containerd
        type: Directory
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
                container('dind') {
                    sh 'docker save homelab-test:latest | ctr -n k8s.io images import -'
                }
            }
        }
        stage('Debug ls') {
            steps {
                sh 'ls -lah'
                sh 'ls -lah charts || true'
            }
        }
        stage('Helm deploy') {
            steps {
                container('helm') {
                    sh 'helm upgrade --install homelab-test ./helm --set image.repository=homelab-test --set image.tag=latest --set image.pullPolicy=Never --namespace homelab'
                }
            }
        }
    }
}
