job('ListJSON_pipeline_develop_subjobs_jobDSL/ListJSON_scm') {

    label('git')

    logRotator {
      numToKeep(30)
      artifactNumToKeep(10)
    }

    scm {
         git {
             remote {
                 github('blobbsen/thesis-ListJSON', 'ssh')
                 credentials('blobbsen_jenkins')
             }
             branch('develop')
         }
     }

    steps {

        shell('''
              tar -zcvf ListJSON.tgz ListJSON
              tar -zcvf ListJSONtest.tgz ListJSONtest

              rm -rf .git/
              rm -rf ListJSON/
              rm -rf ListJSONtest/
        '''.stripIndent())
    }

    publishers {
      archiveArtifacts('*.tgz')
    }
}
