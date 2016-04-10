multiJob('ListJSON_pipeline_develop_jobDSL') {

    label('git')

    logRotator {
        numToKeep(30)
        artifactNumToKeep(10)
    }

    steps {

        phase('scm') {
            phaseJob('ListJSON_pipeline_develop_subjobs_jobDSL/ListJSON_scm') {
            }
        }

        phase('build') {
            phaseJob('ListJSON_pipeline_develop_subjobs_jobDSL/ListJSON_build_develop') {
            }
        }

        phase('test') {
            phaseJob('ListJSON_pipeline_develop_subjobs_jobDSL/ListJSON_monkey_device')
            phaseJob('ListJSON_pipeline_develop_subjobs_jobDSL/ListJSON_unit-test')

            configure { phaseJobConfig ->
              phaseJobConfig / enableCondition << 'false'
              phaseJobConfig / condition << '${RUN_JOB} == "true"'
            }
        }

        // copying all artifacts from started jobs
        copyArtifacts('ListJSON_pipeline_develop_subjobs_jobDSL/ListJSON_build_develop') {
              includePatterns('*.xml')
              targetDirectory('.')
              flatten()
              buildSelector {
                  latestSaved()
              }
        }

        copyArtifacts('ListJSON_pipeline_develop_subjobs_jobDSL/ListJSON_unit-test') {
              includePatterns('*.xml')
              targetDirectory('.')
              flatten()
              buildSelector {
                  latestSaved()
              }
        }

        copyArtifacts('ListJSON_pipeline_develop_subjobs_jobDSL/ListJSON_monkey_device') {
              includePatterns('*.txt')
              targetDirectory('.')
              flatten()
              buildSelector {
                  latestSaved()
              }
        }

    }

    publishers {
        androidLint('*lint.xml')
        archiveJunit('*report.xml')
        archiveArtifacts('*.txt')
    }

}
