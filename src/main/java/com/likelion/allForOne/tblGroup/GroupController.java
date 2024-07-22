package com.likelion.allForOne.tblGroup;

import com.likelion.allForOne.tblGroup.dto.GroupRequestDto;
import com.likelion.allForOne.tblGroup.service.GroupServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/group")
public class GroupController {
    private final GroupServiceImpl groupService;

    /**
     * 방(그룹) 생성
     * @param data GroupRequestDto.saveOneGroup:방(그룹) 생성시 필요한 데이터
     * @param request HttpServletRequest:세션 로그인 정보
     * @return ResponseEntity<?>
     */
    @PostMapping("/create/group")
    public ResponseEntity<?> saveOneGroup(@RequestBody GroupRequestDto.saveOneGroup data, HttpServletRequest request){
//        HttpSession session = request.getSession(false);
//        Long userSeq = (Long) session.getAttribute("userSeq");
        Long userSeq = 1L;
        return ResponseEntity.ok().body(groupService.saveOneGroup(data, userSeq));
    }
}
