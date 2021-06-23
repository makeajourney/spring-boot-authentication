package kr.makeajourney.authentication

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import kr.makeajourney.authentication.domain.member.Role
import kr.makeajourney.authentication.dto.AuthToken
import kr.makeajourney.authentication.dto.MemberDto
import kr.makeajourney.authentication.dto.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    @Value("\${auth.secret-key}")
    private val secretKey: String,
) {

    private val mapper = jacksonObjectMapper()
    private val encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    private val accessTokenValidSeconds: Long = 60 * 60      // 60 min
    private val refreshTokenValidSeconds: Long = 7 * 24 * 60 * 60    // 7 days
    private val TOKEN_TYPE = "Bearer "

    fun createToken(member: MemberDto, roles: List<Role>): AuthToken {
        val accessToken = createTokenString(member, roles, accessTokenValidSeconds)
        val refreshToken = createTokenString(member, roles, refreshTokenValidSeconds)

        return AuthToken(accessToken = accessToken,
            expiresIn = accessTokenValidSeconds,
            refreshToken = refreshToken,
            refreshExpiresIn =  refreshTokenValidSeconds,
            tokenType = TOKEN_TYPE)
    }

    private fun createTokenString(member: MemberDto, roles: List<Role>, validSecond: Long): String {

        val claims = Jwts.claims().setSubject(mapper.writeValueAsString(member))
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
        val claims = getClaimsBodyFromToken(token)
        val member = mapper.readValue(claims.subject) as MemberDto
        val userDetails = User(id = member.id, email = member.email, role = claims.getRole())
        return UsernamePasswordAuthenticationToken(userDetails, "", claims.getRole())
    }

    fun Claims.getRole(): Collection<GrantedAuthority> {

        val token = this["roles"] as List<String>
        return token.map { SimpleGrantedAuthority(it) }
    }

    fun resolveToken(request: HttpServletRequest): String =
        request.getHeader("Authorization")?.replace(TOKEN_TYPE, "") ?: ""

    fun validateToken(token: String): Boolean {
        val claims = getClaimsBodyFromToken(token)
        return claims.expiration.after(Date())
    }

    fun getClaimsBodyFromToken(token: String) : Claims =
        Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(token).body

    fun Long.secondsToMicrosecond(): Long = this * 1000L
}
