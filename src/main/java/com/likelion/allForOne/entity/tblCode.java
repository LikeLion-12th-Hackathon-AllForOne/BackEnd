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
public class tblCode extends BaseEntity {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeSeq;       //구분자
    @Column(columnDefinition = "tinyint default 1 not null comment '분류 단위'")
    private Integer codeUnit;   //분류 단위
    @Column(columnDefinition = "varchar(30) not null comment '코드명'")
    private String codeName;    //코드명
    @Column(columnDefinition = "tinyint not null comment '코드 구분값'")
    private Integer codeVal;    //코드 구분값

    @Comment(value="상위 분류 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_parent_seq")
    private tblCode codeParent; //상위 분류 구분자

}
