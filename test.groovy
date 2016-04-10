job('DSL-Tutorial-1-Test') {
    scm {
        git('git://github.com/jgritman/aws-sdk-test.git')
    }
    steps {
        maven('-e clean test')
    }
}
