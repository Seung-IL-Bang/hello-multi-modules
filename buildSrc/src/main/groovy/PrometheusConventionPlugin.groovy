import org.gradle.api.Plugin
import org.gradle.api.Project

class PrometheusConventionPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        project.plugins.withId('java') {
            project.dependencies {
                implementation 'io.micrometer:micrometer-registry-prometheus'
            }
        }
    }
}
