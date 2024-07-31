package com.likelion.allForOne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TblUsedQuestion {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usedQuestionSeq;       //구분자

    @Comment(value="출제일")
    @Column(updatable = false)
    private LocalDate inpDate;  //출제일


    @ManyToOne
    @Comment(value="질문유형 코드 구분자")
    @JoinColumn(name = "code_question_type", nullable = false)
    private TblCode codeQuestionType;           //질문유형 코드 구분자(28:comAll / 29:comTarget / 30:addTarget)
    @ManyToOne
    @Comment(value="질문구분 코드 구분자")
    @JoinColumn(name = "code_question_class", nullable = false)
    private TblCode codeQuestionClass;           //질문구분 코드 구분자

    @ManyToOne
    @Comment(value="공통질문 구분자")
    @JoinColumn(name = "com_question_seq")
    private TblComQuestion comQuestionSeq;  //공통질문 구분자

    @ManyToOne
    @Comment(value="추가질문 구분자")
    @JoinColumn(name = "add_question_seq")
    private TblAddQuestion addQuestionSeq;  //추가질문 구분자

    @ManyToOne
    @Comment(value="그룹 구분자")
    @JoinColumn(name = "group_seq", nullable = false)
    private TblGroup group;              //그룹 구분자

    @ManyToOne
    @Comment(value="질문 대상자 구분자")
    @JoinColumn(name = "member_target")
    private TblGroupMember memberTarget;    //질문대상자 구분자
}
