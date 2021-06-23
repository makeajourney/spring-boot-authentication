package kr.makeajourney.authentication

import kr.makeajourney.authentication.dto.AuthToken
import kr.makeajourney.authentication.dto.LoginRequest
import kr.makeajourney.authentication.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): AuthToken {
        return authService.login(request.username, request.password)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody token: String): AuthToken {
        return authService.getAuthTokenWithRefreshToken(token)
    }
}
