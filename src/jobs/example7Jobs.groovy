import com.dslexample.builder.GradleCiJobBuilder

List developers = ['dev1@example.com', 'dev2@example.com']

new GradleCiJobBuilder()
    .name("T3-DEV-Build")
    .description('An example using a job builder for a Gradle project.')
    .emails(developers)
    .devParamSVN("ok1")
    .devEmail("dheeraj.vandrangi@spglobal.com")
    .build(this)