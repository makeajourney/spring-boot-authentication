package kr.makeajourney.authentication.domain.member

import kr.makeajourney.authentication.domain.auth.RefreshToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
class Member (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true)
    private val email: String,

    private val password: String,

): UserDetails {

    @ElementCollection
    val role = mutableListOf(Role.USER)

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var refreshToken: RefreshToken? = null

    fun addRole(newRole: Role) = role.add(newRole)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = role

    override fun getPassword() = this.password

    override fun getUsername() = this.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}
