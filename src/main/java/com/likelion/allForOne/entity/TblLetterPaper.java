package com.likelion.allForOne.entity;

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
public class TblLetterPaper {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paperSeq;          //구분자
    @Column(columnDefinition = "varchar(100) not null comment '저장된 파일명'")
    private String paperFileName;   //저장된 파일명

    @ManyToOne
    @Comment(value="편지지 유/무료 코드 구분자")
    @JoinColumn(name = "code_paper", nullable = false)
    private TblCode codePaper;      //편지지 유/무료 코드 구분자 (무료 = 1, 유료 = 2)
}
