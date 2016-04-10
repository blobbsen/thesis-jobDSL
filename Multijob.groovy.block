multiJob('example') {
    steps {
        phase('first') {
            job {
                jobName('job-a')
            }
            job('job-b', false, false)
        }
    }
}
