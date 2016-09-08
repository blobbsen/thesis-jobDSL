//TODO: to dir tree traversing and

// TODO: put in function
//def createPipelinesWithinDir(dir)

def projectDir = 'thesis-jobDSL/pipeline/'
File dir = new File("${WORKSPACE}/${projectDir}")

dir.eachFile { pipeline ->
  
  if (!pipeline.isDirectory() && pipeline.name.endsWith('.groovy') ) {

    //String pipelineConfig = ${new File(pipeline.name.replace('.groovy', '.config')).getText('UTF-8')}
 
    pipelineJob(pipeline.name.replace('.groovy', '')) {
      // injecting ${pipeline}.settings
      //"${pipelineConfig}"
      // loading ${pipeline}.groovy
      definition {
          cps {
            script(readFileFromWorkspace("${projectDir}/${pipeline.name}"))
            sandbox()
          }
      }
    }
  }
}
