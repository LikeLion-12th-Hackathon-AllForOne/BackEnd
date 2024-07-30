package com.likelion.allForOne.domain.tblCode.service;

import com.likelion.allForOne.domain.tblCode.TblCodeRepository;
import com.likelion.allForOne.domain.tblCode.dto.CodeResponseDto;
import com.likelion.allForOne.entity.TblCode;
import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import com.likelion.allForOne.global.response.resEnum.SuccessCode;
import com.likelion.allForOne.domain.tblCode.dto.CodeDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CodeServiceImpl implements CodeService {
    private final TblCodeRepository codeRepository;

    /**
     * controller - 공통코드 select box 리스트 조회
     * @param codeName String:1분류 코드명
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> findListSelectList(String codeName) {
        // 1. 1분류 아래 2분류 리스트 조회
        List<CodeDto.simple1> dtoList = findListUnit(1, codeName);

        // 2. 데이터 여부에 따른 반환 값 조정
        return dtoList == null
                ? ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND)
                : ApiResponse.SUCCESS(SuccessCode.FOUND_LIST, new CodeResponseDto.findListUnit(dtoList));
    }

    /**
     * 공통코드 가족 역할 select box 리스트 조회
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> findListFamilySelectList() {
        // 1. 1분류 아래 2분류 리스트 조회
        List<CodeDto.simple1> dtoList = findListUnit(2, "가족");

        // 2. 데이터 여부에 따른 반환 값 조정
        return dtoList == null
                ? ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND)
                : ApiResponse.SUCCESS(SuccessCode.FOUND_LIST, new CodeResponseDto.findListUnit(dtoList));
    }

    /**
     * 상위분류 아래 하위분류 리스트 조회
     * @param codeName String:1분류 코드명
     * @return CodeDto.simple1
     */
    private List<CodeDto.simple1> findListUnit(int parentUnit, String codeName) {
        // 1. 상위분류 중 해당 코드명을 갖고 있는 코드가 있는지 확인
        Optional<TblCode> firstUnit = codeRepository.findByCodeUnitAndCodeName(parentUnit, codeName);
        if (firstUnit.isEmpty()) return null;

        // 2. 1에서 존재하는 경우, 하위 리스트 조회
        List<TblCode> secondUnitList = codeRepository.findByCodeParent_CodeSeq(firstUnit.get().getCodeSeq());

        // 3. 조회된 데이터 dto변환
        if (secondUnitList.isEmpty()) return null;
        List<CodeDto.simple1> dtoList = new ArrayList<>();
        for (TblCode entity:secondUnitList)
            dtoList.add(CodeDto.simple1.builder()
                    .codeSeq(entity.getCodeSeq())
                    .codeName(entity.getCodeName())
                    .build());

        // 4. return
        return dtoList;
    }

    /**
     * 코드 유효성 검사
     * @param parentName String:상위분류 코드명
     * @param codeSeq Long:코드구분자
     * @return boolean
     */
    public boolean codeValidationCheck(String parentName, Long codeSeq){
        //1. codeSeq 로 등록된 코드확인
        Optional<TblCode> codeOpt = codeRepository.findById(codeSeq);
        if(codeOpt.isEmpty()) return false;

        //2. 해당 코드의 상위 분류와 parentName 이 동일 여부 return
        System.out.println("parentName ::"+codeOpt.get().getCodeParent().getCodeName());
        return parentName.equals(codeOpt.get().getCodeParent().getCodeName());
    }

    /**
     * codeUnit 과 부모코드로 코드 조화
     * @param codUnit int:분류 단계
     * @param parentCodeSeq long: 부모코드
     * @return TblCode: code entity
     */
    public TblCode findCodeByUnitAndParentCode(int codUnit, long parentCodeSeq){
        return codeRepository.findByCodeUnitAndCodeParent_CodeSeq(codUnit, parentCodeSeq).orElse(null);
    }

    /**
     * codeSeq로 코드 조회
     * @param codeSeq long: 코드 구분자
     * @return TblCode:code entity
     */
    public TblCode findCodes(long codeSeq){
        return codeRepository.findById(codeSeq).orElse(null);
    }




}
