package kr.makeajourney.authentication.service

import kr.makeajourney.authentication.JwtTokenProvider
import kr.makeajourney.authentication.domain.auth.RefreshToken
import kr.makeajourney.authentication.domain.auth.RefreshTokenRepository
import kr.makeajourney.authentication.domain.member.Member
import kr.makeajourney.authentication.domain.member.MemberRepository
import kr.makeajourney.authentication.dto.AuthToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpClientErrorException
import java.lang.RuntimeException

@Service
class AuthService (
    private val memberRepository: MemberRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: BCryptPasswordEncoder,
) {

    @Transactional(readOnly = true)
    fun findMemberByEmail(email: String): Member {
        return memberRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found")
    }

    @Transactional
    fun login(username: String, password: String): AuthToken {
        val user = findMemberByEmail(username)

        if (passwordEncoder.matches(password, user.password)) {
            throw RuntimeException("wrong password")
        }
        return jwtTokenProvider.createToken(username, user.role)
            .also { user.refreshToken = RefreshToken(member = user, token = it.refreshToken) }
    }

    @Transactional
    fun getAuthTokenWithRefreshToken(refreshToken: String): AuthToken {
        val token = refreshTokenRepository.findByToken(refreshToken) ?: throw RuntimeException("invalid refresh token")
        val user = token.member
        return jwtTokenProvider.createToken(user.username, user.role)
            .also { user.refreshToken = RefreshToken(member = user, token = it.refreshToken) }
    }

    @Transactional
    fun signUp(username: String, password: String) {
        val exist = memberRepository.findByEmail(username)
        if (exist != null) {
            throw RuntimeException("username exists")
        }
        memberRepository.save(Member(email = username, password = passwordEncoder.encode(password)))
    }
}
