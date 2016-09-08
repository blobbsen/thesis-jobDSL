//TODO: to dir tree traversing and

// TODO: put in function
//def createPipelinesWithinDir(dir)

def projectDir = 'thesis-jobDSL/pipeline/'
File dir = new File("${WORKSPACE}/${projectDir}")

dir.eachFile { pipeline ->
  // TODO: check if "if" is necessary in fact of using eachFile
  if (!pipeline.isDirectory() ) {

    String pipelineConfig = new File("${WORKSPACE}/thesis-jobDSL/pipeline/${pipeline.name.replace('.groovy', '.config')}").getText('UTF-8')

    pipelineJob("${pipeline.name.replace('.groovy', '')}") {
      //injectConfig(pipelineConfig)
      definition {
          cps {
            script(readFileFromWorkspace("${projectDir}/${pipeline.name}"))
            sandbox()
          }
      }
    }
  }
}

def injectConfig(config) {
	config
}
