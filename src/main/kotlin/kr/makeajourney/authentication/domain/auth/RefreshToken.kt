package kr.makeajourney.authentication.domain.auth

import kr.makeajourney.authentication.domain.member.Member
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne
    val member: Member,

    val token: String,
)
