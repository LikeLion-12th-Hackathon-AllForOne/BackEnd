package com.likelion.allForOne.entity;

import com.likelion.allForOne.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class tblAnswer extends BaseEntity {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerSeq;         //구분자
    @Column(columnDefinition = "varchar(500) null comment '답변'")
    private String answerContents;  //답변


    @Comment(value="질문 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_question_seq", nullable = false)
    private tblUsedQuestion usedQuestionSeq;    //질문 구분자

    @Comment(value="답변 생성자 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_answer", nullable = false)
    private tblGroupMember memberAnswer;        //답변 생성자 구분자

    @Comment(value="질문 대상자 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_target", nullable = false)
    private tblGroupMember memberTarget;        //질문 대상자 구분자
}
