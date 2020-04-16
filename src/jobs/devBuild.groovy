def devParamSVN = 'trunk'
def devParamCondtionNo = 'N'
def devParamCondtionYes = 'Y'
def devParamDBOption = 'A'
def devParamGeneric = 'Yes'
def jobName = 'T3-DEV-Build'
def param1svn = 'SVN'
def param2db = 'DB'
def param3tag = 'TAG'
def param4prevtag = 'PREV_TAG'
def param5dbobj = 'DBOBJECT'
def devEmail = 'T3Developers@spglobal.com'
def replyList = 'arunkumar.sivakumar@spglobal.com,dheeraj.vandrangi@spglobal.com'

job(jobName) {
	description()
	keepDependencies(false)
	parameters {
		stringParam(param1svn, devParamSVN, "")
		stringParam(param2db, devParamCondtionNo, "")
		stringParam(param3tag, devParamCondtionNo, "")
		stringParam(param4prevtag, devParamCondtionNo, "")
		stringParam(param5dbobj, devParamDBOption, "")
		wHideParameterDefinition {
      name('generic')
      defaultValue(devParamGeneric)
      description('Generic')
	  }
	}
	wrappers {
        maskPasswords()
    }
	wrappers {
        buildUserVars()
    }
	disabled(false)
	concurrentBuild(false)
	steps {
		shell("""source ~/.bash_profile
cd /local/apps/t3app/deployment
./t3_package_script.sh""")
	}
	publishers {
		textFinder(/No such file or directory | TCP connection reset by peer/, '', true, false, true)
		mailer(devEmail, false, false)
		extendedEmail {
			recipientList(devEmail)
			triggers {
				always {
					recipientList(devEmail)
					subject("\$PROJECT_DEFAULT_SUBJECT")
					content("\$PROJECT_DEFAULT_CONTENT")
					attachmentPatterns()
					attachBuildLog(false)
					compressBuildLog(false)
					contentType("text/html")
				}
				beforeBuild {
					recipientList(devEmail)
					subject("JENKINS | T3 | AWS_DEVOPS DEV | Build | Start")
					content("This is to notify that Dev Build has been started.")
					attachmentPatterns()
					attachBuildLog(false)
					compressBuildLog(false)
					contentType("text/html")
			}
				notBuilt {
				recipientList(devEmail)
					subject("\$PROJECT_DEFAULT_SUBJECT")
					content("There seems to be an issue with Artifactory/Compilation-error. We are working on this and will send you an update once it is resolved.")
					attachmentPatterns()
					attachBuildLog(false)
					compressBuildLog(false)
					contentType("text/html")
			}			
			}
			contentType("text/html")
			defaultSubject("Build Status of | \$JOB_NAME")
			defaultContent("\$DEFAULT_CONTENT")
			attachmentPatterns()
			preSendScript("\$DEFAULT_PRESEND_SCRIPT")
			attachBuildLog(false)
			compressBuildLog(false)
			replyToList(replyList)
			saveToWorkspace(false)
			disabled(false)
		}
	}
}