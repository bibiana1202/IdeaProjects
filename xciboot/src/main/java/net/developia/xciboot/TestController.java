package net.developia.xciboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 뷰를 거치지 않고 다이렉트로 뷰에 전달
public class TestController {

    @GetMapping("test")
    public String test(){
        return "Hello World";
    }
}
