pipeline {
agent any
stages {
stage ('Compile Stage') {
steps {
maven(name : 'maven3') {
bat'mvn clean compile'
}
}
}
stage ('Testing Stage') {
steps {
maven(name : 'maven3') {
bat'mvn test'
}
}
}
stage ('Install Stage') {
steps {
maven(name : 'maven3') {
bat'mvn install'
}
}
}
}
}