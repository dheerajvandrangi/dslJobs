package com.dslexample.builder

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

/**
 * Example Class for creating a Gradle build
 */
@Builder(builderStrategy = SimpleStrategy, prefix = '')
class GradleCiJobBuilder {

	def devParamSVN
	def devParamCondtionNo = 'o'
	def devParamCondtionYes = 'p'
	def devParamDBOption = 'bNo'
	def devParamGeneric = 'n'
	def param1svn = 'sss'
	def param2db = 'ss'
	def param3tag = 'bb'
	def param4prevtag = 'asddc'
	def param5dbobj = 'sds'
	def devEmail
	def tScript = 'abc.sh'
    String name
    String description
    String ownerAndProject
    String gitBranch = 'master'
    String pollScmSchedule = '@daily'
    String tasks
    String switches
    Boolean useWrapper = true
    String junitResults = '**/build/test-results/*.xml'
    String artifacts = '**/build/libs/*.jar'
    List<String> emails = []

    Job build(DslFactory dslFactory) {
        dslFactory.job(name) {
            it.description this.description
//            logRotator {
//                numToKeep 5
//            }
// 	         scm {
//                github this.ownerAndProject, gitBranch
 //          }
            parameters {
		stringParam(param1svn, devParamSVN, "")
		stringParam(param2db, devParamCondtionNo, "")
		stringParam(param3tag, devParamCondtionNo, "")
		stringParam(param4prevtag, devParamCondtionNo, "")
		stringParam(param5dbobj, devParamDBOption, "")
	}
            wrappers {
        maskPasswords()
    }
    wrappers {
        buildUserVars()
    }
            steps {
                gradle tasks, switches, useWrapper
            }
            steps {
		shell("""source ~/.bash_profile
cd /local/apps/abcd
./${tScript}""")
	}
	configure {
		it / 'properties' / 'jenkins.model.BuildDiscarderProperty' {
			strategy {
				'daysToKeep'('1')
				'numToKeep'('10')
				'artifactDaysToKeep'('-1')
				'artifactNumToKeep'('-1')
			}
		}
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
			saveToWorkspace(false)
			disabled(false)
		}
	}
        }
    }
}
