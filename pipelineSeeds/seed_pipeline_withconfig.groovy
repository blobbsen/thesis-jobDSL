job('seed-job') {
  	
	description('seedJob for uffda')  
  	label('master')
  
  	steps {
    
      	// getting only jobs from SCM
      	shell('''
rm -rf ${WORKSPACE}/*
git clone https://github.com/blobbsen/thesis-jobDSL.git
ls -al thesis-jobDSL/pipeline/
''')
      	// creating folder for jobs
        dsl {
          for(int i=1;i<10;i++){
            folder("${i}") {
    			description('Folder containing all QA jobs for project A')
          	}
          }
       }
          
      	// deploying jobs
        dsl {
            external('thesis-JobDSL/*.groovy')
            removeAction('DISABLE')
          	ignoreExisting(true)
        }
    }

    // TODO: test if it triggers after deploy via HTTP request
    //queue()
}
