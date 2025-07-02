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
      image: maven:3.9.6-eclipse-temurin-17
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
        // többi stage: docker build, ctr, helm, stb.
    }
}
