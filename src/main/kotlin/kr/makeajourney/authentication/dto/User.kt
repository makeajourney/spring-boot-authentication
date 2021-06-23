package kr.makeajourney.authentication.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class User(
    val id: Long = 0,
    val email: String,
    val role: Collection<GrantedAuthority> = listOf(),
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = role

    override fun getPassword() = ""

    override fun getUsername() = this.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}
