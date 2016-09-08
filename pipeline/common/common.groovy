{ ->
 echo 'loading common.groovy to make steps available within this pipeline'
}

def hello(whom) {
  sh "echo \"hello ${whom}\""
}

def wholeMasterStep() {
stage 'testDat'
node('master') {
  sh 'injected via common.groovy'
}
}

return this;
