package kr.makeajourney.authentication.service

import kr.makeajourney.authentication.domain.member.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MemberUserDetailsService(
    val memberRepository: MemberRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepository.findByEmail(username) ?: Un
    }
}
