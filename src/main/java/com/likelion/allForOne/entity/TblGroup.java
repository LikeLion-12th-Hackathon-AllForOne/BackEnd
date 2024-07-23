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
public class TblGroup extends BaseEntity {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupSeq;          //구분자
    @Column(columnDefinition = "tinyint not null comment '인원수'")
    private Integer groupMemberCnt; //인원수
    @Column(columnDefinition = "varchar(10) not null comment '그룹명'")
    private String groupName;       //그룹명
    @Column(columnDefinition = "varchar(8) not null comment '그룹 초대코드'")
    private String groupInviteCode; //그룹 초대코드


    @ManyToOne
    @Comment(value="카테고리 구분자")
    @JoinColumn(name = "code_category", nullable = false)
    private TblCode codeCategory;   //카테고리 구분자

    @ManyToOne
    @Comment(value="방장 사용자 구분자")
    @JoinColumn(name = "user_owner", nullable = false)
    private TblUser userOwner;      //방장 사용자 구분자

}
