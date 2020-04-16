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
            logRotator {
                numToKeep 5
            }
// 	         scm {
//                github this.ownerAndProject, gitBranch
 //          }
            triggers {
                scm pollScmSchedule
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
cd /local/apps/t3app/deployment
./t3_package_script.sh""")
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
                archiveArtifacts artifacts
                archiveJunit junitResults
                if (emails) {
                    mailer emails.join(' ')
                }
                
            }
        }
    }
}
