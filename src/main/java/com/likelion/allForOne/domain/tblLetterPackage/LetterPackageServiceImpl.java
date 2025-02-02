package com.likelion.allForOne.domain.tblLetterPackage;

import com.likelion.allForOne.entity.TblGroup;
import com.likelion.allForOne.entity.TblLetterPackage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LetterPackageServiceImpl {
    private final TblLetterPackageRepository letterPackageRepository;

    /**
     * 선물 보따리 생성
     * @param group TblGroup:그룹 entity
     * @return Long:선물보따리 구분자
     */
    @Transactional
    public Long saveLetterPackage(TblGroup group){
        int randomObjective = createObjective(group.getGroupMemberCnt());
        TblLetterPackage letterPackage = TblLetterPackage.builder()
                .packageObjective(randomObjective)
                .packageCnt(0)
                .packageStartDate(group.getCreateDate())
                .group(group)
                .build();
        return letterPackageRepository.save(letterPackage).getPackageSeq();
    }

    /**
     * 보따리 달성 개수 랜덤 생성
     * @param groupMemberCnt int:그룹 참여 인원수
     * @return int:보따리 달성 개수(packageObjective)
     */
    private int createObjective(int groupMemberCnt){
        //최소 = groupMemberCnt*5 / 최대 = 최소+20
        int min = groupMemberCnt*5;
        int max = min+20;

        // (int)(Math.random() * (최댓값-최소값+1)) + 최소값
        return ((int)(Math.random()*(max-min+1))+min);
    }

    /**
     * 편지보따리 달성도 조회
     * @param groupSeq Long: 그룹 구분자
     * @return int:달성도
     */
    public double packageAchievePercent(Long groupSeq){
        Optional<TblLetterPackage> letterPackageOpt = letterPackageRepository.findByGroup_GroupSeq(groupSeq);
        if (letterPackageOpt.isEmpty()) return 0;

        double packageCnt = letterPackageOpt.get().getPackageCnt();                //작성된 편지 개수
        double packageObjective = letterPackageOpt.get().getPackageObjective();    //보따리 달성 개수

        double achievePercent = packageCnt/packageObjective; //달성도
        return Math.round(achievePercent * 10.0) / 10.0;
    }

}
