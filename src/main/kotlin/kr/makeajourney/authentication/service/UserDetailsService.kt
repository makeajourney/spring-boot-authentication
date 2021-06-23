package kr.makeajourney.authentication.service

import kr.makeajourney.authentication.dto.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsService : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return User(email = username)
    }
}
