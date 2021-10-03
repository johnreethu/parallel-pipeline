pipeline {
    agent any

    stages {
        stage ('Stg1'){
            steps {
                echo "Step 1"
            }
        }
        stage ('Stg2'){
            parallel
            {
                stage("2.1"){
                    steps {
                        echo "This is step 2.1"
                    }
                }
                stage("2.2"){
                    steps {
                        echo "This is step 2.2"
                    }
                }
            }
        }
        stage ('Stg3') {

            when {
                not {
                    branch "master"
                }
            }
            steps {
                    echo "Run if not master"
            }
            
        }
    }
}
