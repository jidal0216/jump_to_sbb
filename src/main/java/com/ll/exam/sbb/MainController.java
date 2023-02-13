package com.ll.exam.sbb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {
    @RequestMapping("/sbb")
    // 아래 함수의 리턴값을 그대로 브라우저에 표시
    // 아래 함수의 리턴값을 문자열화 해서 브라우저 응답의 바디에 담는다.
    @ResponseBody

    public String index() {
        //서버에서 출력
        System.out.println("Hello");
        //먼 미래에 브라우저에서 보여짐
        return "안녕";
    }


    @GetMapping("/page1")
    @ResponseBody
    public String showPage1() {
        return """
                <form method="POST" action="/page2">
                     <input type="number" name="age" placeholder="나이" />
                     <input type="submit" value="page2로 POST 방식으로 이동" />
                </form>
                 """;
    }

    @PostMapping("/page2")
    @ResponseBody
    public String showPage2Post(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나의 : %d</h1>
                <h1>안녕하세요, POST 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    @GetMapping("/page2")
    @ResponseBody
    public String showPage2Get(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나의 : %d</h1>
                <h1>안녕하세요, POST 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    private int increaseNo = -1;

    @GetMapping("/increase")
    @ResponseBody
    public int showIncrease() {
        increaseNo++;

        return increaseNo;

    }


    @GetMapping("/Gugudan")
    @ResponseBody
    public String showGugudan(Integer dan, Integer limit) {
        if (limit == null) {
            limit = 9;
        }
        if (dan == null) {
            dan = 9;
        }
        Integer finalDan = dan;
        return IntStream.rangeClosed(1, limit)
                .mapToObj(i -> "%d * %d = %d".formatted(finalDan, i, finalDan * i))
                .collect(Collectors.joining("<br>\n"));
    }

    @GetMapping("/plus2")
    @ResponseBody
    public void showPlus2(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int a = Integer.parseInt(req.getParameter("a"));
        int b = Integer.parseInt(req.getParameter("b"));
        try (PrintWriter printWriter = resp.getWriter().append(a + b + "")) {
        }


    }

    @GetMapping("/mbti/{name}")
    @ResponseBody
    public String showMbti(@PathVariable String name) {
        String rs = switch (name) {
            case "홍길순" -> {
                char j = 'J';
                yield "INF" + j;
            }
            case "임꺽정" -> "INTP";
            case "정지원", "홍길동" -> "INTJ";
            default -> "모름";
        };
        return rs;
    }

    @GetMapping("/saveSession/{name}/{value}")
    @ResponseBody
    public String saveSession(@PathVariable String name, @PathVariable String value, HttpServletRequest req) {
        HttpSession session = req.getSession();

        session.setAttribute(name, value);

        return "세션변수 %s의 값이 %s(으)로 설정이 되었습니다.".formatted(name, value);
    }

    @GetMapping("/getSession/{name}")
    @ResponseBody
    public String getSession(@PathVariable String name, HttpSession session) {
        String value = (String) session.getAttribute(name);

        return "세션변수 %s의 값이 %s 입니다.".formatted(name, value);
    }


    @GetMapping("/addArticle")
    @ResponseBody
    public String addArticle(String title, String body) {

        Article article = new Article(title, body);

        return "%d번 게시물이 생성 되었습니다.".formatted(article.getId());
    }
}
@AllArgsConstructor
class Article{
    private static int lastId = 0;
    @Getter
    private final int id;
    private final String title;
    private final String body;

    public Article(String title, String body) {
        this(++lastId,title,body);
    }
}




