package com.likelion.allForOne.domain.tblQuestion;

import com.likelion.allForOne.domain.tblQuestion.dto.QuestionRequestDto;
import com.likelion.allForOne.domain.tblQuestion.service.QuestionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {
    private final QuestionServiceImpl questionService;
    private final HttpServletRequest request;

    /**
     * 질문 추가하기
     * @param data QuestionRequestDto.AddQuestion:사용자가 추가한 질문 사항
     * @return ResponseEntity<?>
     */
    @PostMapping("/add")
    public ResponseEntity<?> addQuestion(@RequestBody QuestionRequestDto.AddQuestion data){
        HttpSession session = request.getSession(false);
        Long userSeq = (Long) session.getAttribute("userSeq");
        return ResponseEntity.ok().body(questionService.addQuestion(data, userSeq));
    }

}
