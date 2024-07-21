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
public class tblComQuestion extends BaseEntity {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comQuestionSeq;    //구분자
    @Column(columnDefinition = "varchar(50) not null comment '질문 내용'")
    private String comQuestion;     //질문 내용

    @ManyToOne
    @Comment(value="질문 코드 구분자")
    @JoinColumn(name = "code_question", nullable = false)
    private tblCode codeQuestion;    //질문 코드 구분자
}
