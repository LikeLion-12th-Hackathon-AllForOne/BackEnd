package com.likelion.allForOne.domain.tblQuestion;

import com.likelion.allForOne.domain.tblQuestion.dto.QuestionRequestDto;
import com.likelion.allForOne.domain.tblQuestion.service.QuestionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 답변 임시저장
     * @param data QuestionRequestDto.SaveAnswer
     * @return ResponseEntity<?>
     */
    @PostMapping("/tempSave")
    public ResponseEntity<?> tempSaveAnswer(@RequestBody QuestionRequestDto.SaveAnswerList data){
        HttpSession session = request.getSession(false);
        Long userSeq = (Long) session.getAttribute("userSeq");
        return ResponseEntity.ok().body(questionService.saveAnswer(1, data, userSeq));
    }

    /**
     * 답변 등록
     * @param data QuestionRequestDto.SaveAnswer
     * @return ResponseEntity<?>
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveAnswer(@RequestBody QuestionRequestDto.SaveAnswerList data){
        HttpSession session = request.getSession(false);
        Long userSeq = (Long) session.getAttribute("userSeq");
        return ResponseEntity.ok().body(questionService.saveAnswer(0, data, userSeq));
    }

    /**
     * 오늘의 질문 답변(임시저장 or 저장)
     * @param usedQuestionSeq Long: 오늘의 질문 구분자
     * @param memberSeq Long: 멤버 구분자
     * @return ResponseEntity<?>
     */
    @GetMapping("/today/question/{usedQuestionSeq}/answer/{memberSeq}")
    public ResponseEntity<?> findTodayQandA(@PathVariable("usedQuestionSeq") Long usedQuestionSeq,
                                            @PathVariable("memberSeq") Long memberSeq){
        HttpSession session = request.getSession(false);
        Long userSeq = (Long) session.getAttribute("userSeq");
        return ResponseEntity.ok().body(questionService.findTodayQandA(usedQuestionSeq, memberSeq, userSeq));
    }

}
