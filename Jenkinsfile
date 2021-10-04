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
		DOCKERHUB = 'hub.docker.com'
        	DOCKERIMAGE = 'johnreethu/parallel-pipeline' 
        	DOCKERCREDENTIALS= credentials('repo-access-token')
        	CI = 'true'
        	GITHUB-REPO = 'github.com/johnreethu/parallel-pipeline'
		APP-NAME = 'DockerFile'
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
		//checkout([$class: 'GitHub', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '', url: $GITHUB-REPO]]])      
            }
      }
        
        stage ('Build')
        {
            //To experiment the parallel branching and build of the code. The code is compiled using different versions of the Java Compiler.
	    parallel
            {
              stage ('Build with Java 8') 
              
                {
                /*    agent 
                    {
                        label 'java8'
                    }
                */    
                    steps 
                    {
                        //Maven is being called through the Agent with the label "java8' that is tagged in the label section of Agent configuration. Same applies to below secment too.
			//sh 'mvn compile'
                        echo "This is my build step"
                    }
                }  
                stage ('build with Java 11') 
                {
                 /*  agent 
                    {
                        label 'java11'
                    }
                 */   
                    steps 
                    {
                        //sh 'mvn compile'
                        echo "This is my build step"
                    }
                
                }  
                
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
                    echo "This is my build step"
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
                        		sh 'docker build -t $GITHUB-REPO/$APP-NAME .'
                    		}
                	}
                	stage('Login into docker hub') 
			{
                    		steps 
		    		{
                        		sh 'echo $DOCKERCREDENTIALS_PSW | docker login $DOCKERHUB -u $DOCKERCREDENTIALS_USR --password-stdin'
                    		}
                	}
			     
                	stage('Push the image to docker hub') 
			{
                
                    		steps 
		  		{
					sh '''
					docker tag $GITHUB-REPO/$APP-NAME $DOCKERIMAGE:v-$BUILD_NUMBER
					docker push $DOCKERIMAGE:v-$BUILD_NUMBER
					'''
                    		}
                	}
            }

            
            post  ('logout')
            {
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
	post 
	 {
        	failure 
		 {
            		echo "Build Numbe: " $BUILD_NO
			echo "is failed"
			echo "For more details, Please refer below URL"
			echo $JENKINS_URL
			
        	}
    	}  
    }
}
