job('job_DSL/DSL-Tutorial-2-Test') {
    scm {
        git('git://github.com/jgritman/aws-sdk-test.git')
    }
    steps {
        maven('-e clean test')
    }
}
