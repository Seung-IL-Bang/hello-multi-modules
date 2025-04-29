
import org.gradle.api.Plugin
import org.gradle.api.Project

class ActuatorConventionPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        project.plugins.withId('java') {
            project.dependencies {
                implementation 'org.springframework.boot:spring-boot-starter-actuator'
            }
        }
    }
}
