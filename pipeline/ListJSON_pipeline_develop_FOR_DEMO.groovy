node('git') {

   checkout changelog: false, scm: [$class: 'GitSCM', branches: [[name: '*/develop']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'e109ee24-bbbe-462a-bb26-c544d291a026', url: 'git@github.com:blobbsen/thesis-ListJSON.git']]]

   sh  '''
        tar -zcvf ListJSON.tgz ListJSON
        tar -zcvf ListJSONtest.tgz ListJSONtest
        rm -rf .git/
        rm -rf ListJSON/
        rm -rf ListJSONtest/
   '''
   archive '*.tgz'
}

node('androidbuild') {
    unarchive mapping: ['*.tgz' : '.']

   sh '''export PATH=${PATH}:${ANDROID_HOME}/platform-tools:${ANDROID_HOME}/tools


tar -xzvf ListJSON.tgz > extraction.log && rm ListJSON.tgz
tar -xzvf ListJSONtest.tgz >> extraction.log && rm ListJSONtest.tgz


echo "building"

cd ListJSON && ./debugBuild.sh jenkins && lint --xml ListJSON_lint.xml .

cd ../ListJSONtest && ./debugBuild.sh jenkins && lint --xml ListJSONtest_lint.xml .

cd ..

cp ListJSON/bin/MainActivity-debug.apk .
cp ListJSONtest/bin/ListJSONtest-debug.apk .
cp ListJSON/ListJSON_lint.xml .
cp ListJSONtest/ListJSONtest_lint.xml .
rm -rf ListJSON/
rm -rf LIstJSONtest

'''

   archive '*.apk, *.xml'
}


//parallel ({

    node('androidtest') {
        unarchive mapping: ['*.apk' : '.']

        sh '''
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
        '''
        archive '*.xml'
    }
    //},{
    node('androidtest') {
        unarchive mapping: ['*.apk' : '.']

        sh '''
            PACKAGE="me.blobb.listjson"
            APK="MainActivity-debug.apk"

            adb uninstall $PACKAGE
            adb install $APK

            adb shell monkey -p $PACKAGE -v 500 > monkey.txt

            adb uninstall $PACKAGE
            cat monkey.txt
        '''
        archive 'monkey.txt'
    }
    //})

node('git'){
    unarchive mapping: ['*.xml' : '.']
    step([$class: 'JUnitResultArchiver', testResults: '*report.xml'])
    //build job: 'build \'ListJSON_debug_pipeline_android_lint_stats\'', wait: false
    build job: 'ListJSON_debug_pipeline_android_lint_stats', wait: false

}
