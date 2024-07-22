package com.likelion.allForOne.tblCode;

import com.likelion.allForOne.tblCode.service.CodeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
public class CodeController {
    private final CodeServiceImpl codeService;

    /**
     * 공통코드 select box 리스트 조회
     * @param codeName String:1분류 코드명
     * @return ResponseEntity<?>
     */
    @GetMapping("/{codeName}/selectList")
    public ResponseEntity<?> findListSelectList(@PathVariable("codeName") String codeName){
        return ResponseEntity.ok().body(codeService.findListSelectList(codeName));
    }

}
