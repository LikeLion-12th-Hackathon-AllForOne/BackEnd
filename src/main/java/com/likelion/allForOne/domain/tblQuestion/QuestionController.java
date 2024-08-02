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
     * 오늘의 질문 답변(임시저장 or 저장) 조회
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

    /**
     * 지난 오늘의 퀴즈 모아보기 (7개씩)
     * @param groupSeq Long: 방(그룹) 구분자
     * @return ResponseEntity<?>
     */
    @GetMapping("/last/{groupSeq}/questionList/{inpDate}")
    public ResponseEntity<?> findLastQandA(@PathVariable("groupSeq") Long groupSeq, @PathVariable("inpDate") String inpDate){
        HttpSession session = request.getSession(false);
        Long userSeq = (Long) session.getAttribute("userSeq");
        return ResponseEntity.ok().body(questionService.findLastQandA(groupSeq, userSeq, inpDate));
    }

    /**
     * 특정 그룹에서 특정인에 대한 퀴즈 모아보기
     * @param memberTargetSeq Long: 질문 대상자 멤버 구분자
     * @return ResponseEntity<?>
     */
    @GetMapping("/someone/{memberTargetSeq}/questionList/{inpDate}")
    public ResponseEntity<?> findSomeoneQAndA(@PathVariable("memberTargetSeq") Long memberTargetSeq, @PathVariable("inpDate") String inpDate){
        HttpSession session = request.getSession(false);
        Long userSeq = (Long) session.getAttribute("userSeq");
        return ResponseEntity.ok().body(questionService.findSomeoneQAndA(memberTargetSeq, userSeq, inpDate));
    }

}
