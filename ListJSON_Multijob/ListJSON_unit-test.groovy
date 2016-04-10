job('ListJSON_pipeline_develop_subjobs_jobDSL/ListJSON_unit-test') {

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
                  LastSuccessful()
              }
        }

        shell('''
              PACKAGE="me.blobb.listjson"
              TEST_PACKAGE="${PACKAGE}.test"
              APK="MainActivity-debug.apk"
              TEST_APK="ListJSONtest-debug.apk"

              adb uninstall $PACKAGE
              adb uninstall $TEST_PACKAGE

              adb install $APK
              adb install $TEST_APK

              adb shell am instrument -w -e reportFile junit-report.xml  ${TEST_PACKAGE}/com.zutubi.android.junitreport.JUnitReportTestRunner
              adb pull /data/data/${PACKAGE}/files/junit-report.xml

              adb uninstall $PACKAGE
              adb uninstall $TEST_PACKAGE
        '''.stripIndent())
    }

    publishers {

      archiveArtifacts('*.xml')
      archiveJunit('*.xml')
  }

}
