pipeline 
{
    agent
    {
        node 
	    {
            label 'maven'    
        }
    }
    
    environment 
	{
        //Set the environmental variables to be used in the script below.
		//Docker Hub location where the file will be pushed to.
		DOCKER_HUB = 'hub.docker.com' 
		//This is the credential stored in Jenkins Global Credentials with "docker_id"
        	DOCKERCREDENTIALS = credentials('docker_id')
        	CI = 'true'
        	IMAGE_NAME = 'johnreethu/parallel-pipeline'
		GITHUB_LINK = 'github.com/johnreethu/parallel-pipeline'
		APP_NAME = 'BMICalculator'
		
	}
	
	options 
	{
		// Set the time limit for Pipeline with overall timeout for the build of one hour.
		timeout(time: 1, unit: 'HOURS')
		// When we have test-fails e.g. we don't need to run the remaining steps
		skipStagesAfterUnstable()
    }
	
	
    stages 
	//These are the srages defined in the the pipeline. All the stages will be executed for the branches
	//"main", "test","deploy"
	//As per the branching strategy, final stage pushing the the file to artifactory will be done only in "main" branch
    {
        stage ('checkout') 
        {
            steps 
            {
                //This will checkout the code from Repo.
		        echo "This is my CheckOut step"
		    //checkout([$class: 'GitHub', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '', url: $GITHUB_LINK]]])      
            }
        }
        
        stage ('Build')
        {
          steps 
	        {
                sh 'mvn compile'
            }  
        }
       
        stage ('test') 
        {
            //Testing block of the pipeline. The app is being tested through Maven Comments.
	        parallel
            {
                stage ('Unit Test') 
                {
                    steps 
                        {
                            sh 'mvn clean test'
                            echo "This is my unit test"
                        }
            
                }  
                stage ('Code Coverage') 
                {    
                    steps 
                    {
                        sh 'mvn clean verify'
                        echo "This is my build step"
                    }
                    
                }  
                stage ('System Test') 
                {
                    //This is the sample segment only for parallel pipeline. Since it is simple native java application, unit test covers the test scenarios.
                    steps 
                    {
                        sh 'java -version'
                        sh 'java'
                        sh 'javac'
                        sh 'mvn -v'
                        sh 'docker -v'
                        sh 'systemctl status docker'
                    }
                
                }  
            } 
        }
       
        
        stage('Build-Push') 
	    {
            
            // The code below is with "docker' label as mentioned in Agent1. Any agent with a label name "docker" will be picked and code will be executed.
            //The code is executed only for "main" branch.
	        //Values are fetched from environment variables.
            agent 
		    {
                node 
                	{
                    		label 'docker' 
                	} 
            }
            when 
		    {
               	branch 'main'
            }
            stages 
		    {
                stage('buid image') 
			    {
                    steps 
				    {
                        sh 'docker build -t $IMAGE_NAME/$APP_NAME .'
                    }
                }
                stage('Login into docker hub') 
			    {
                    steps 
		    		{
                        sh 'echo $DOCKERCREDENTIALS_PSW | docker login $DOCKER_HUB -u $DOCKERCREDENTIALS_USR --password-stdin'
                    }
                }
			     
                stage('Push the image to docker hub') 
			    {
                    //Tag the file and push it to DockerHub.
                    steps 
		  		    {
					    sh 'docker tag $IMAGE_NAME/$APP_NAME $IMAGE_NAME/$APP_NAME:VERSION-$BUILD_NUMBER'
					    sh 'docker push $IMAGE_NAME/$APP_NAME:VERSION-$BUILD_NUMBER'	
                    }
        
                    
                }
            }

            
            post  ('logout')
            {
                //It is the best practice to logout from docker
		        always 
                {
                    sh 'docker logout'
                    echo "Logout from Docker Hub by using plugin"
                }
            }
            
        }
	
	    //Dummy Code for Production deployment.
        stage ('production') 
        {
            steps 
            {
                echo "This is my Production step"
            }
        }

       
	   
    }
    post ('final message')
    {
        failure 
        {
            //using the parameters generated from Jenkins
            echo 'Build Number: $BUILD_NO'
            echo "is failed"
            echo "For more details, Please refer below URL"
            echo '$JENKINS_URL'
        
        }
    }  

}
