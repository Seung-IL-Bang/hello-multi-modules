
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints
 */
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
