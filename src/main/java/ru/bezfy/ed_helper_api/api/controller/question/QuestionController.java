package ru.bezfy.ed_helper_api.api.controller.question;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.QuestionModel;
import ru.bezfy.ed_helper_api.model.enums.UserType;
import ru.bezfy.ed_helper_api.service.QuestionService;

import java.util.List;

@Controller
@RequestMapping("/v1/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("all")
    public ResponseEntity<List<QuestionModel>> getQuestions() {
        return questionService.getQuestions();
    }

    @GetMapping("user")
    public ResponseEntity<List<QuestionModel>> getQuestionsForUser(@AuthenticationPrincipal LocalUser user) {
        return questionService.getQuestionByUserId(user.getId());
    }

    @PostMapping("create")
    public ResponseEntity<String> createQuestion(@AuthenticationPrincipal LocalUser user, @RequestParam String question) {
        return questionService.createQuestion(user, question);
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> deleteQuestion(@AuthenticationPrincipal LocalUser user, @RequestParam Long id) {
        if (!user.getUserType().equals(UserType.TEACHER) && !user.getUserType().equals(UserType.ADMIN)) {
            return ResponseEntity.badRequest().body("You are not allowed to delete this question");
        }
        return questionService.deleteQuestion(id);
    }
}
