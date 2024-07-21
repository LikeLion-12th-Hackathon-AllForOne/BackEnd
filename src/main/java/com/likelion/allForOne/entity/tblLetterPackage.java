package com.likelion.allForOne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class tblLetterPackage {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageSeq;                //구분자
    @Column(columnDefinition = "tinyint not null comment '보따리 달성 개수'")
    private Integer packageObjective;       //보따리 달성 개수
    @Column(columnDefinition = "tinyint default 0 not null comment '작성된 편지 개수'")
    private Integer packageCnt;             //작성된 편지 개수
    @CreatedDate
    @Comment(value="보따리 시작일시")
    @Column(updatable = false)
    private LocalDateTime packageStartDate; //보따리 시작일시

    @Comment(value="그룹 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_seq", nullable = false)
    private tblGroup groupSeq;              //그룹 구분자
}
