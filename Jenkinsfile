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
        DOCKERHUB = 'hub.docker.com'
        DOCKERIMAGE = 'johnreethu/parallel-pipeline' 
        DOCKERCREDENTIALS= credentials('docker_id')
        CI = 'true'
        GITHUB-REPO = 'johnreethu/parallel-pipeline'     
	}
    stages 
    {
        stage ('checkout') 
        {
            steps 
            {
                echo "This is my CheckOut step"
            }
        }
        
        stage ('Build')
        {
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
                steps 
                {
                    echo "This is my build step"
                }
            
            }  
        } 
      }
       
        
        stage('Build-Push') 
	    {
            
            // any agent with a label docker will be picked
            
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
                        sh 'docker build -t $GitHub/$app .'
                    }
                }
                stage('Login into docker hub') 
			    {
                    steps 
				    {
                        sh 'echo $DockerCredentials_PSW | docker login $DockerHub -u $DockerCredentials_USR --password-stdin'
                    }
                }
                stage('Push the image to docker hub') 
			    {
                
                    steps 
					{
                        sh '''
                        docker tag $GitHub/$app $DockerImage:v-$BUILD_NUMBER
                        docker push $DockerImage:v-$BUILD_NUMBER
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
        stage ('production') 
        {
            steps 
            {
                echo "This is my Production step"
            }
        }
    }
}
