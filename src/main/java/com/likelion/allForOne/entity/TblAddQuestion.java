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
public class TblAddQuestion extends BaseEntity {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeSeq;                   //구분자
    @Column(columnDefinition = "varchar(50) not null comment '질문 내용'")
    private String addQuestion;             //질문 내용

    @ManyToOne
    @Comment(value="질문 코드 구분자")
    @JoinColumn(name = "code_question", nullable = false)
    private TblCode codeQuestion;           //질문 코드 구분자

    @Comment(value="질문 생성자 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_create", nullable = false)
    private TblGroupMember memberCreate;    //질문 생성자 구분자
}
