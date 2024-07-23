package com.likelion.allForOne.tblGroup;

import com.likelion.allForOne.tblGroup.dto.GroupRequestDto;
import com.likelion.allForOne.tblGroup.service.GroupServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/group")
public class GroupController {
    private final GroupServiceImpl groupService;
//    private final HttpServletRequest request;

    /**
     * 방(그룹) 생성
     * @param data GroupRequestDto.saveOneGroup:방(그룹) 생성시 필요한 데이터
     * @return ResponseEntity<?>
     */
    @PostMapping("/create/group")
    public ResponseEntity<?> saveOneGroup(@RequestBody GroupRequestDto.saveOneGroup data){
//        HttpSession session = request.getSession(false);
//        Long userSeq = (Long) session.getAttribute("userSeq");
        Long userSeq = 1L;
        return ResponseEntity.ok().body(groupService.saveOneGroup(data, userSeq));
    }

    /**
     * 참여 방(그룹) 리스트
     * @return ResponseEntity<?>
     */
    @GetMapping("/findList/joinGroup")
    public ResponseEntity<?> findListJoinGroup(){
//        HttpSession session = request.getSession(false);
//        Long userSeq = (Long) session.getAttribute("userSeq");
        Long userSeq = 1L;
        return ResponseEntity.ok().body(groupService.findListJoinGroup(userSeq));
    }

    /**
     * 초대코드 조회
     * @param groupSeq Long:그룹 구분자
     * @return ResponseEntity<?>
     */
    @GetMapping("/{groupSeq}/findInviteCode")
    public ResponseEntity<?> findGroupInviteCode(@PathVariable("groupSeq") Long groupSeq){
//        HttpSession session = request.getSession(false);
//        Long userSeq = (Long) session.getAttribute("userSeq");
        Long userSeq = 1L;
        return ResponseEntity.ok().body(groupService.findGroupInviteCode(groupSeq, userSeq));
    }
}
