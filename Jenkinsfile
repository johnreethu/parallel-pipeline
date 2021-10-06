/*
Script Name : Parallel-Pipeline
Purpose : Generate Automated Pipeline for multibranch repo from GitHub. Also deal build issues through Pull Requests when the jobs has issues.
Author: John Richard / LNo:l00163194 LYIT Master's Student (M.Sc DevOps)
Repo: https://github.com/johnreethu/parallel-pipeline/
Date of Completion : 05-Oct-2021
Reference Online Material: https://www.jenkins.io/doc/book/pipeline/syntax/ (Pipeline Syntax)
Reference YouTube: https://www.youtube.com/watch?v=qQS7Idaq_ME&list=PLvBBnHmZuNQJeznYL2F-MpZYBUeLIXYEe (CloudBees TV - Darin Pope)
Reference Github: https://github.com/darinpope/multibranch-sample-app
Reference Github: https://github.com/rlennon/Parallel_Jenkins_Demo
Further References : Lecture Notes given by Ruth Lennon, online references, blogs & YouTube videos.
*/

pipeline 
{
  //Provide agent label that are entered in nodes in jenkins for explicit reference to specific node or "agent any" can be added.
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
		//This is the credential stored in Jenkins Global Credentials with "docker_id"
        	DOCKERCREDENTIALS = credentials('docker_id')        	
        	REPO_NAME = 'johnreethu/parallel-pipeline'
		GITHUB_LINK = 'github.com/johnreethu/parallel-pipeline'
		IMAGE_NAME = 'bmi_calculator'
		
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
                	//This will checkout the code from Repo, but it is commented as we are using webhook for the code.
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
                        echo "This is my build step. Maven executed and completed the test"
                    }
                    
                }  
                stage ('System Test') 
                {
                    //This is the sample segment only for parallel pipeline. Since it is simple native java application, unit test covers the test scenarios. System Integration test can be added later
                    steps 
                    {
                        sh 'mvn -v'
                        sh 'docker -v'
                        sh 'systemctl status docker'
			echo "This is my system test stage. As system test is not required for this app, just printing mvn, docker comments."
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
                        sh 'docker build -t $REPO_NAME:$APP_NAME .'
                    }
                }
		//In this stage, the script will login to the Docker Hub by supplying credential that are set up in Jenkins Global Credentials.	    
                stage('Login into docker hub') 
			    {
                    steps 
		    		{
                        sh 'echo $DOCKERCREDENTIALS_PSW | docker login -u $DOCKERCREDENTIALS_USR --password-stdin'
                    }
                }
			     
                stage('Push the image to docker hub') 
			    {
				    //Tag the file that is from the repo docker file and push it to DockerHub. Tagging is always important to identify the image.
				    //Multiple tags can be added. Push comment will place the image in Docker Hub with the tag information.
				    //Tag may be a string or global variables from jenkins such as $BUILD_NO,$JOB_ID,$JOB_NAME,$NODE_NAME,$NODE_LABELS
                    		steps 
		  		    {
					    sh 'docker tag $REPO_NAME:$IMAGE_NAME $REPO_NAME:$IMAGE_NAME-$JOB_NAME'
					    sh 'docker push $REPO_NAME:$IMAGE_NAME-$JOB_NAME'	
                    			}      
                    
                	}
            }

            
            post  ('logout from dockerhub')
            {
                //It is the best practice to logout from docker to ensure the security is imposed.
		always 
                {
                    sh 'docker logout'
                    echo 'Logout from Docker Hub by using plugin'
                }
            }
            
        }
	
	//Dummy Code for Production deployment.
        stage ('production') 
        {
            steps 
            {
                echo 'This is my Production step. This can be step when publishing to servers, cloud, etc.'
            }
        }       
	   
    }
    //Post information in the screen in case of build failed. More global variables can be added. The can be sent through an email or short messages.
    //SMTP server info to be configured for sending mails.
    post ('final message')
    {
        failure 
        {
            //using the global variables generated from Jenkins
            echo "current build number: ${currentBuild.number}"
            echo "is failed"
            echo "For more details, Please refer below URL"
	    echo "Jenkins URL: ${JENKINS_URL}"
        
        }
    }  

}
