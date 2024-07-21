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
public class TblGroupMember extends BaseEntity {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSeq;     //구분자

    @Comment(value="그룹 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_seq", nullable = false)
    private TblGroup groupSeq;  //그룹 구분자

    @Comment(value="사용자 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private TblUser userSeq;  //사용자 구분자

    @ManyToOne
    @Comment(value="편지보따리 확인코드 구분자")
    @JoinColumn(name = "code_package", nullable = false)
    private TblCode codePackage;  //편지보따리 확인코드 구분자
}
