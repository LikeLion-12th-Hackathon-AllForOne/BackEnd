package com.likelion.allForOne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class tblUsedQuestion {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeSeq;       //구분자
    @CreatedDate
    @Comment(value="출제일")
    @Column(updatable = false)
    private LocalDate inpDate;  //출제일


    @ManyToOne
    @Comment(value="질문 코드 구분자")
    @JoinColumn(name = "code_question", nullable = false)
    private tblCode codeQuestion;   //질문코드 구분자

    @Comment(value="공통질문 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "com_question_seq")
    private tblComQuestion comQuestionSeq;//공통질문 구분자

    @Comment(value="질문 생성자 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_question_seq")
    private tblAddQuestion addQuestionSeq;//추가질문 구분자

    @Comment(value="그룹 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_seq", nullable = false)
    private tblGroup groupSeq;              //그룹 구분자

    @Comment(value="질문 생성자 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_target")
    private tblGroupMember memberTarget;    //질문대상자 구분자
}
