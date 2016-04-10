job('banch_develop_test/ListJSON_monkey_device') {

    label('androidtest')

    logRotator {
      numToKeep(30)
      artifactNumToKeep(10)
    }

    steps {

        copyArtifacts('branch_develop_test/ListJSON_build_develop') {
              includePatterns('*.apk')
              targetDirectory('.')
              flatten()
              buildSelector {
                  upstreamBuild(false)
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
