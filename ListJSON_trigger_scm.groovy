job('banch_develop_test/ListJSON_build_develop') {

    label('git')

    logRotator {
      numToKeep(30)
      artifactNumToKeep(10)
    }

    scm {
         github('blobbsen/thesis-ListJSON.git','*/develop')
    }

    steps {

        sh('''
              tar -zcvf ListJSON.tgz ListJSON
              tar -zcvf ListJSONtest.tgz ListJSONtest

              rm -rf .git/
              rm -rf ListJSON/
              rm -rf ListJSONtest/
        '''.stripIndent())

    }

    publishers {
      archiveArtifacts('*.tgz')
      buildPipelineTrigger('branch_develop_test/ListJSON_build_develop)
  }
