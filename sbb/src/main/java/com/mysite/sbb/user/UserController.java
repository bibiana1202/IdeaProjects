package com.mysite.sbb.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        // 패스워드 불일치
        if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2","passwordIncorrect","2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }


        // 중복 회원 방지
        try{
            userService.create(userCreateForm.getUsername(),
                               userCreateForm.getEmail(),
                               userCreateForm.getPassword1());
        }
        catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed","이미 등록된 사용자 입니다.");
            return "signup_form";
        }
        catch(Exception e){
            e.printStackTrace();
            bindingResult.reject("singupFailed",e.getMessage());
            return "signup_form";

        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    //실제 로그인을 진행하는 @PostMapping 방식의 메서드는 스프링 시큐리티가 대신 처리하므로 우리가 직접 코드를 작성하여 구현할 필요가 없다.
}
