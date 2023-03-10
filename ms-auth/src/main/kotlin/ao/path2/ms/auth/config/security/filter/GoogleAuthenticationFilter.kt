package ao.path2.ms.auth.config.security.filter

import ao.path2.ms.auth.config.security.model.GoogleUserData
import ao.path2.ms.auth.core.JWSAuthToken
import ao.path2.ms.auth.handlers.ErrorDetails
import ao.path2.ms.auth.repository.UserRepository
import ao.path2.ms.auth.token.JwtToken
import ao.path2.ms.auth.utils.security.getGoogleAuthURL
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDateTime
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class GoogleAuthenticationFilter(
  private val restTemplate: RestTemplate,
  private val jwtToken: JwtToken,
  private val userRepository: UserRepository
) : OncePerRequestFilter(), SocialAuthenticationFilter {

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

    val googleToken = extractTokenIfNotNull(request)

    if (googleToken != null) {
      try {
        //get data from Facebook

        val headers = HttpHeaders()

        headers.set("Authorization", "Bearer $googleToken")

        val httpEntity = HttpEntity("", headers)

        val res =
          restTemplate.exchange(getGoogleAuthURL(), HttpMethod.GET, httpEntity, GoogleUserData::class.java)

        if (res.statusCode == HttpStatus.OK) {
          val body = res.body

          if (!userRepository.existsByEmail(body?.email!!)) {
            populateResponse(
              response, ErrorDetails(
                401,
                "Cannot authenticate user",
                LocalDateTime.now(),
                "Invalid token"
              ), HttpStatus.UNAUTHORIZED
            )
            return
          }

          val user = userRepository.findByEmail(body.email!!)

          val responseToken = jwtToken.generateToken(user.username)

          populateResponse(response, JWSAuthToken(responseToken), HttpStatus.OK)

          return

        }
      } catch (err: Exception) {
        populateResponse(
          response,
          ErrorDetails(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            err.message,
            LocalDateTime.now(),
            err.cause.toString()
          ),
          HttpStatus.INTERNAL_SERVER_ERROR
        )

        filterChain.doFilter(request, response)
        return
      }

    }

    filterChain.doFilter(request, response)

  }

  override fun extractTokenIfNotNull(request: HttpServletRequest): String? = request.getHeader("google_token")
}