package kr.makeajourney.authentication

import kr.makeajourney.authentication.dto.LoginRequest
import kr.makeajourney.authentication.dto.User
import kr.makeajourney.authentication.service.MemberService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController (
    private val memberService: MemberService,
) {

    @PostMapping("/signup")
    fun signUp(@RequestBody request: LoginRequest) {
        memberService.signUp(request.username, request.password)
    }

    @GetMapping("/member/me")
    fun me(@AuthenticationPrincipal user: User): String {
        return user.email
    }
}
