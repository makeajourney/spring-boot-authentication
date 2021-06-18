package kr.makeajourney.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class MemberAuthenticationFilter : UsernamePasswordAuthenticationFilter() {

    private val mapper = ObjectMapper()

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        return super.attemptAuthentication(request, response)
    }
}
