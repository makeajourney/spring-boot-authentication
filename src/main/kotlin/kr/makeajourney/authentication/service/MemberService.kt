package kr.makeajourney.authentication.service

import kr.makeajourney.authentication.domain.member.Member
import kr.makeajourney.authentication.domain.member.MemberRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

@Service
class MemberService (
    private val memberRepository: MemberRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
){
    @Transactional
    fun signUp(username: String, password: String) {
        val exist = memberRepository.findByEmail(username)
        if (exist != null) {
            throw RuntimeException("username exists")
        }
        memberRepository.save(Member(email = username, password = bCryptPasswordEncoder.encode(password)))
    }
}
