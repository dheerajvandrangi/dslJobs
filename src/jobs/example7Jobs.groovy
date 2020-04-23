import com.dslexample.builder.GradleCiJobBuilder

String basePath = 'example7'
List developers = ['dev1@example.com', 'dev2@example.com']

folder(basePath) {
    description 'This example shows how to create jobs using Job builders.'
}

new GradleCiJobBuilder()
    .name("T3-DEV-Build")
    .description('An example using a job builder for a Gradle project.')
    .emails(developers)
    .devParamSVN("ok1")
    .build(this)