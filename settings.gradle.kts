rootProject.name = "springboot-simple-template"

include(
    "template-infrastructure:jpa",
    "template-infrastructure:feign",
    "template-common",
    "template-server"
)

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.github.com/disdong123/version-catalog")
            credentials {
                // PAT, github username 을 환경변수 (.zshrc 등)로 저장해야합니다.
                username = System.getenv("DISDONG_USERNAME")
                password = System.getenv("DISDONG_TOKEN")
            }
        }
    }
    versionCatalogs {
        create("libs") {
            from("kr.disdong:spring-version-catalog:0.0.31")
        }
    }
}
