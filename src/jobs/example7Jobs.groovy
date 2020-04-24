import com.dslexample.builder.GradleCiJobBuilder

List developers = ['dev1@example.com', 'dev2@example.com']

new GradleCiJobBuilder()
    .name("T3-DEV-Build")
    .description('This is can be used for Dev Build')
    .emails(developers)
    .devParamSVN("ok1")
    .devEmail("dhefvsdsdedsdsrasdfsj.vandssdfsdfrdssdangi@spgsfsdflobsfsfal.com")
    .build(this)