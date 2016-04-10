job('ListJSON_pipeline_develop_subjobs_jobDSL/ListJSON_monkey_device') {

    label('androidtest')

    logRotator {
      numToKeep(30)
      artifactNumToKeep(10)
    }

    steps {

        copyArtifacts('ListJSON_pipeline_develop_subjobs_jobDSL/ListJSON_build_develop') {
              includePatterns('*.apk')
              targetDirectory('.')
              flatten()
              buildSelector {
                  latestSaved()
              }
        }

        shell('''
              PACKAGE="me.blobb.listjson"
              APK="MainActivity-debug.apk"

              adb uninstall $PACKAGE
              adb install $APK
              adb shell monkey -p $PACKAGE -v 500 > monkey.txt
              adb uninstall $PACKAGE
        '''.stripIndent())
    }

    publishers {
       archiveArtifacts('*.txt')
    }

}
