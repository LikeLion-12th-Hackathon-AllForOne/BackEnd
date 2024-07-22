package com.likelion.allForOne.tblCode.service;

import com.likelion.allForOne.entity.TblCode;
import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import com.likelion.allForOne.global.response.resEnum.SuccessCode;
import com.likelion.allForOne.tblCode.TblCodeRepository;
import com.likelion.allForOne.tblCode.dto.CodeDto;
import com.likelion.allForOne.tblCode.dto.CodeResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CodeServiceImpl implements CodeService {
    private final TblCodeRepository codeRepository;

    /**
     * 1분류 아래 2분류 리스트 조회
     * @param codeName String:1분류 코드명
     * @return CodeDto.simple1
     */
    @Override
    public List<CodeDto.simple1> findListUnit2(String codeName) {
        // 1. 1분류 중 해당 코드명을 갖고 있는 코드가 있는지 확인
        Optional<TblCode> firstUnit = codeRepository.findByCodeUnitAndCodeName(1, codeName);
        if (firstUnit.isEmpty()) return null;

        // 2. 1에서 존재하는 경우, 2분류 리스트 조회
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
     * controller - 공통코드 select box 리스트 조회
     * @param codeName String:1분류 코드명
     * @return ApiResponse<?>
     */
    public ApiResponse<?> findListSelectList(String codeName) {
        // 1. 1분류 아래 2분류 리스트 조회
        List<CodeDto.simple1> dtoList = findListUnit2(codeName);

        // 2. 데이터 여부에 따른 반환 값 조정
        if (dtoList.isEmpty())
            return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
        else
            return ApiResponse.SUCCESS(SuccessCode.FOUND_LIST, new CodeResponseDto.findListUnit2(dtoList));
    }
}
