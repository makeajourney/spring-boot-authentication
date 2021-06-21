package kr.makeajourney.authentication.service

import kr.makeajourney.authentication.domain.member.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService(
    val memberRepository: MemberRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User not found")
    }
}
