pipelineJob('dslTestFolder/example_2') {
    	description('uffda')
	definition {
        cps {
            script('''
			node('master') {
				echo 'hallo'
			}
		   ''')
            sandbox()
        }
    }
}
