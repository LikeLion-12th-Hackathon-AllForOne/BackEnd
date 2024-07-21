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
public class TblUser extends BaseEntity {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;       //구분자
    @Column(columnDefinition = "varchar(10) not null comment '아이디'")
    private String userId;      //아이디
    @Column(columnDefinition = "varchar(13) not null comment '비밀번호'")
    private String userPwd;     //비밀번호
    @Column(columnDefinition = "varchar(20) not null comment '이름'")
    private String userName;    //이름
    @Column(columnDefinition = "char(8) not null comment '생년월일'")
    private String userBirth;   //생년월일
    @Column(columnDefinition = "varchar(11) not null comment '전화번호'")
    private String userPhone;   //전화번호
    @Column(columnDefinition = "varchar(30) null comment '개인 프로필 이미지 파일명'")
    private String userImg;     //개인 프로필 이미지 파일명

    @ManyToOne
    @Comment(value="mbti 구분자")
    @JoinColumn(name = "code_mbti", nullable = false)
    private TblCode codeMbti;   //mbti 구분자
}
