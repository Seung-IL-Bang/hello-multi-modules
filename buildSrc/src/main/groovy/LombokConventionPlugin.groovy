import org.gradle.api.Plugin
import org.gradle.api.Project

class LombokConventionPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.plugins.withId('java') {
            project.dependencies {
                def lombokVersion = project.findProperty("lombokVersion") ?: "1.18.30"
                compileOnly "org.projectlombok:lombok:$lombokVersion"
                annotationProcessor "org.projectlombok:lombok:$lombokVersion"
                testCompileOnly "org.projectlombok:lombok:$lombokVersion"
                testAnnotationProcessor "org.projectlombok:lombok:$lombokVersion"
            }
        }
    }
}
