package kr.makeajourney.authentication

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import kr.makeajourney.authentication.domain.member.Role
import kr.makeajourney.authentication.dto.AuthToken
import kr.makeajourney.authentication.service.UserDetailsService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    @Value("\${auth.secret-key}")
    private val secretKey: String,
    private val service: UserDetailsService,
) {

    private val encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    private val accessTokenValidSeconds: Long = 60 * 60      // 60 min
    private val refreshTokenValidSeconds: Long = 7 * 24 * 60 * 60    // 7 days
    private val TOKEN_TYPE = "Bearer"

    fun createToken(username: String, roles: List<Role>): AuthToken {
        val accessToken = createTokenString(username, roles, accessTokenValidSeconds)
        val refreshToken = createTokenString(username, roles, refreshTokenValidSeconds)

        return AuthToken(accessToken = accessToken,
            expiresIn = accessTokenValidSeconds,
            refreshToken = refreshToken,
            refreshExpiresIn =  refreshTokenValidSeconds,
            tokenType = TOKEN_TYPE)
    }

    private fun createTokenString(username: String, roles: List<Role>, validSecond: Long): String {

        val claims = Jwts.claims().setSubject(username)
        claims["roles"] = roles
        val now = Date()

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + validSecond.secondsToMicrosecond()))
            .signWith(SignatureAlgorithm.HS256, encodedSecretKey)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = service.loadUserByUsername(getSubject(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getSubject(token: String): String =
        getClaimsBodyFromToken(token).subject

    fun resolveToken(request: HttpServletRequest): String =
        request.getHeader("Authorization")

    fun validateToken(token: String): Boolean {
        val claims = getClaimsBodyFromToken(token)
        return claims.expiration.after(Date())
    }

    fun getClaimsBodyFromToken(token: String) : Claims =
        Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(token).body

    fun Long.secondsToMicrosecond(): Long = this * 1000L
}
