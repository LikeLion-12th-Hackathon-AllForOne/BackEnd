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
public class tblLetter extends BaseEntity {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long letterSeq;         //구분자
    @Column(columnDefinition = "varchar(30) null comment '누구에게'")
    private String letterTo;        //누구에게
    @Column(columnDefinition = "varchar(30) null comment '누구로부터'")
    private String letterFrom;      //누구로부터
    @Column(columnDefinition = "varchar(1500) not null comment '편지내용'")
    private String letterContents;  //편지내용
    @Column(columnDefinition = "tinyint default 0 not null comment '편지읽음여부'")
    private Integer letterRead;     //편지읽음여부


    @Comment(value="받는사람 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_to", nullable = false)
    private tblGroupMember memberTo;    //받는사람 구분자

    @Comment(value="보내는사람 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_from", nullable = false)
    private tblGroupMember memberFrom;  //보내는사람 구분자

    @Comment(value="편지지 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paper_seq", nullable = false)
    private tblLetterPaper paperSeq;  //편지지 구분자
}
