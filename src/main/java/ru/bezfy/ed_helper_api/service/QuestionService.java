package ru.bezfy.ed_helper_api.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.QuestionModel;
import ru.bezfy.ed_helper_api.model.dao.QuestionModelDAO;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionModelDAO questionModelDAO;

    public QuestionService(QuestionModelDAO questionModelDAO) {
        this.questionModelDAO = questionModelDAO;
    }

    public ResponseEntity<List<QuestionModel>> getQuestions() {
        return ResponseEntity.ok(questionModelDAO.findAll());
    }
    public ResponseEntity<List<QuestionModel>> getQuestionByUserId(Long id) {
        Optional<List<QuestionModel>> questions = questionModelDAO.findAllByUserId(id);
        return questions.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<String> createQuestion(LocalUser user, String question) {
        QuestionModel questionModel = new QuestionModel();
        questionModel.setUser(user);
        questionModel.setText(question);
        questionModelDAO.save(questionModel);
        return ResponseEntity.ok("Question added");
    }

    public ResponseEntity<String> deleteQuestion(Long id) {
        questionModelDAO.deleteById(id);
        return ResponseEntity.ok("Question deleted");
    }

}
