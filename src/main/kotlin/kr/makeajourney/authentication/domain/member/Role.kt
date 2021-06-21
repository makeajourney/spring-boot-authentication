package kr.makeajourney.authentication.domain.member

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    USER,
    ;

    override fun getAuthority() = this.toString()
}
