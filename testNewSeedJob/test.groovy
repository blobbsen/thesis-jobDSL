pipelineJob('example') {
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
