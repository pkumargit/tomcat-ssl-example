node {
  stage('Checkout SCM') {
    checkout scm
  }
  stage('Maven Install') {
    sh "mvn clean install"
  }
  stage('Sonar Scan') {
    sh "mvn clean compile sonar:sonar -Dsonar.host.url=http://localhost:9000"
  }
  stage('Build Dockerfile') {
    if (env.CHANGE_TARGET == "master") {
      sh "/usr/local/bin/docker login -u 'pkuma343' -p 'Ponkmonk_138202'"
      sh "/usr/local/bin/docker build -t 'pkuma343/tomcat-ssl-example:develop' ."
      sh "/usr/local/bin/docker push 'pkuma343/tomcat-ssl-example:develop'"
    }
  }
  stage('Deploy to Openshift') {
    if (env.CHANGE_TARGET == "master") {
    sh "/usr/local/bin/oc login 'https://console.starter-us-east-1.openshift.com:443' -u 'pkuma343' -p 'Ponkmonk_138202' --insecure-skip-tls-verify"
    sh "/usr/local/bin/oc new-project 'tomcat-ssl-example'"
    sh "/usr/local/bin/oc project 'tomcat-ssl-example'"
    // sh "/usr/local/bin/oc create -f apidemo.yaml"
    sh "/usr/local/bin/oc new-app --docker-image='pkuma343/tomcat-ssl-example:develop'"
    }
  }
}
