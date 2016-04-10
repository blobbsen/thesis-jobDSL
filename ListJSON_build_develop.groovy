job('banch_develop_test/ListJSON_build_develop') {

    label('androidbuild')

    logRotator {
      numToKeep(30)
      artifactNumToKeep(10)
    }

    steps {

        copyArtifacts('branch_develop_test/ListJSON_trigger_scm') {
              includePatterns('*.tgz')
              targetDirectory('.')
              flatten()
              buildSelector {
                  upstreamBuild(false)
              }
        }

        shell('''
              export PATH=${PATH}:${ANDROID_HOME}/platform-tools:${ANDROID_HOME}/tools
              ls -al
              tar -xzvf ListJSON.tgz && rm ListJSON.tgz
              tar -xzvf ListJSONtest.tgz && rm ListJSONtest.tgz
              ls -al

              echo "building"
              cd ListJSON && ./debugBuild.sh jenkins && lint --xml ListJSON_lint.xml .
              cd ../ListJSONtest && ./debugBuild.sh jenkins && lint --xml ListJSONtest_lint.xml .

              cd ${WORKSPACE}
              cp ListJSON/bin/MainActivity-debug.apk .
              cp ListJSONtest/bin/ListJSONtest-debug.apk .
              cp ListJSON/ListJSON_lint.xml .
              cp ListJSONtest/ListJSONtest_lint.xml .
              rm -rf ListJSON/
              rm -rf LIstJSONtest
              ls -al
        '''.stripIndent())
    }

    publishers {
      androidLint('*lint.xml')

      archiveArtifacts('*.apk')
      archiveArtifacts('*.xml')

      downstream('branch_develop_test/ListJSON_monkey_device', 'SUCCESS')
      downstream('branch_develop_test/ListJSON_unit-test', 'SUCCESS')
      downstream('branch_develop_test/ListJSON_overview_debug', 'SUCCESS')
  }

}
