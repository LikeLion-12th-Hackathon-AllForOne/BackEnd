package com.likelion.allForOne.domain.tblGroup;

import com.likelion.allForOne.domain.tblGroup.dto.GroupRequestDto;
import com.likelion.allForOne.domain.tblGroup.service.GroupServiceImpl;
import com.likelion.allForOne.login.utils.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/group")
public class GroupController {
    private final GroupServiceImpl groupService;

    /**
     * 방(그룹) 생성
     * @param data GroupRequestDto.saveOneGroup:방(그룹) 생성시 필요한 데이터
     * @return ResponseEntity<?>
     */
    @PostMapping("/create")
    public ResponseEntity<?> saveOneGroup(@RequestBody GroupRequestDto.saveOneGroup data, Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userSeq = userDetails.getUserSeq();
        return ResponseEntity.ok().body(groupService.saveOneGroup(data, userSeq));
    }

    /**
     * 참여 방(그룹) 리스트
     * @return ResponseEntity<?>
     */
    @GetMapping("/findList/joinGroup")
    public ResponseEntity<?> findListJoinGroup(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userSeq = userDetails.getUserSeq();
        return ResponseEntity.ok().body(groupService.findListJoinGroup(userSeq));
    }

    /**
     * 초대코드 조회
     * @param groupSeq Long:그룹 구분자
     * @return ResponseEntity<?>
     */
    @GetMapping("/{groupSeq}/findInviteCode")
    public ResponseEntity<?> findGroupInviteCode(@PathVariable("groupSeq") Long groupSeq, Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userSeq = userDetails.getUserSeq();
        return ResponseEntity.ok().body(groupService.findGroupInviteCode(groupSeq, userSeq));
    }

    /**
     * 초대코드 입장-회원 등록
     * @param data GroupRequestDto.saveGroupMemberByInviteCode:입력받은 초대코드
     * @return ResponseEntity<?>
     */
    @PostMapping("/join")
    public ResponseEntity<?> saveGroupMemberByInviteCode(@RequestBody GroupRequestDto.saveGroupMemberByInviteCode data, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userSeq = userDetails.getUserSeq();
        return ResponseEntity.ok().body(groupService.saveGroupMemberByInviteCode(data, userSeq));
    }

    /**
     * 그룹 정보 상세 조회(수정필요)
     * @param groupSeq Long:그룹 구분자
     * @return ResponseEntity<?>
     */
    @GetMapping("/{groupSeq}/findDetails")
    public ResponseEntity<?> findGroupDetail(@PathVariable("groupSeq") Long groupSeq, Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userSeq = userDetails.getUserSeq();
        return ResponseEntity.ok().body(groupService.findGroupDetail(groupSeq, userSeq));
    }

    /**
     * 가족그룹에서의 역할 update
     * @param data GroupRequestDto.updateRole
     * @return ResponseEntity<?>
     */
    @PostMapping("/updateRole")
    public ResponseEntity<?> updateRole(@RequestBody GroupRequestDto.updateRole data, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userSeq = userDetails.getUserSeq();
        return ResponseEntity.ok().body(groupService.updateRole(data, userSeq));
    }
}
