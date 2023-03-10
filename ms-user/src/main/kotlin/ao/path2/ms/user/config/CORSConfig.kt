package ao.path2.ms.user.config

import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


class CORSConfig {

  /*@Value("\${cors.originPatterns:default}")
  private val corsOriginPatterns: String = "*"*/


  fun addCorsConfig(): WebMvcConfigurer {
    return object : WebMvcConfigurer {
      override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedMethods("*").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
      }
    }
  }


  fun corsConfigurationSource(): CorsConfigurationSource? {
    val configuration = CorsConfiguration()
    configuration.allowedOrigins = listOf("*")
    configuration.allowedMethods = listOf("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD")
    configuration.allowCredentials = true
    configuration.allowedHeaders = listOf("*")
    configuration.exposedHeaders = listOf("X-Get-Header")
    configuration.maxAge = 3600L
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", configuration)
    return source
  }

}