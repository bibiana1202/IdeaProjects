package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;


@RequestMapping("/question")
@RequiredArgsConstructor // 애너테이션의 생성자 방식
// 롬복에서 제공하는 애너테이션으로 , final이 붙은 속성을 포함하는 생성자를 자동으로 만들어주는 역할
// 따라서 스프링부트가 내부적으로 QuestionController를 생성할때 롬복으로 만들어진 생성자에 의해 questionRepository 객체가 자동으로 주입된다.
@Controller
public class QuestionController {

    // RequiredArgsConstructor 애너테이션 생성자 방식으로 questionRepository 객체를 주입
    //private final QuestionRepository questionRepository;

    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
//    @ResponseBody
    public String list(Model model, @RequestParam(value="page",defaultValue = "0") int page,@RequestParam(value="kw",defaultValue="") String kw) {
        // model 객체는 자바 클래스와 템플릿 간의 연결고리 역할
        // model 객체에 값을 담아두면 템플릿에서 그값을 사용할수 있따.
        // model 객체는 따로 생성할 필요 없이 컨트롤러의 메서드에 매개변수로 지정하기만 하면 스프링 부트가 자동으로 mdoel 객체를 생성
//        List<Question> questionList = questionRepository.findAll();
        // 페이징 처리
        Page<Question> paging = this.questionService.getList(page,kw);
        model.addAttribute("paging",paging);
        model.addAttribute("kw",kw);
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question",question);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm){
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(),questionForm.getContent(),siteUser); // 질문 저장
        return "redirect:/question/list"; // 질문 저장후 질문 목록으로 이동
    }

    // 질문 컨트롤러 수정 하기 1
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    // 질문 컨트롤러 수정 하기 2
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    // 질문 삭제하기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    // 추천
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id){
        Question question = this.questionService.getQuestion(id);
        SiteUser siteuser = this.userService.getUser(principal.getName());
        this.questionService.vote(question,siteuser);
        return String.format("redirect:/question/detail/%s", id);
    }

}
