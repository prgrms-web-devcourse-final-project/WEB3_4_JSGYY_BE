package com.ll.nbe344team7.domain.admin.controller;

import com.ll.nbe344team7.domain.admin.dto.BlockMemberDTO;
import com.ll.nbe344team7.domain.admin.dto.CategoryDTO;
import com.ll.nbe344team7.domain.admin.dto.MemberDTO;
import com.ll.nbe344team7.domain.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "어드민 API")
public class AdminController {
//1
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     *
     * 회원 정보 목록 조회
     *
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @Operation(summary = "회원 정보 조회")
    @GetMapping("/members")
    public ResponseEntity<?> getMembers(){
        return ResponseEntity.ok(Map.of("message", "회원 조회 성공",
                "data", List.of(new MemberDTO("홍길동", "길동이", "hong@example.com", false,
                        "2024-03-21T12:00:00", "서울특별시 강남구", "010-1234-1234"))));
    }

    /**
     *
     * 회원 정보 변경
     * - 차단
     *
     * @param block
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @Operation(summary = "회원 정보 변경 - 차단")
    @PutMapping("/members/block")
    public ResponseEntity<?> blockMember(@RequestBody BlockMemberDTO block){
        if(block.getMemberId() == null || block.getMemberId() == 10000){
            return ResponseEntity.status(404).body(Map.of("message", "해당 ID의 회원을 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(Map.of("message", "3번 회원님이 정지되었습니다."));
    }

    /**
     *
     * 카테고리 정보 조회
     *
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @Operation(summary = "카테고리 정보 조회")
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(){
        return ResponseEntity.ok(Map.of("message", "데이터 전달 성공", "data", List.of(new CategoryDTO(1L, "의류"))));
    }

    /**
     *
     * 카테고리 생성
     *
     * @param category
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @Operation(summary = "카테고리 생성")
    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO category){
        return ResponseEntity.ok(Map.of("message", "카테고리 생성 성공"));
    }

    /**
     *
     * 카테고리 수정
     *
     * @param categoryId
     * @param category
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @Operation(summary = "카테고리 수정")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer categoryId, @RequestBody CategoryDTO category){
        if(categoryId == null || categoryId == 10000){
            return ResponseEntity.status(404).body(Map.of("message", "해당 카테고리를 찾을 수 없습니다."));
        }

        return ResponseEntity.ok(Map.of("message", "카테고리 수정 성공"));
    }

    /**
     *
     * 카테고리 삭제
     *
     * @param categoryId
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId){
        if(categoryId == null || categoryId == 10000){
            return ResponseEntity.status(404).body(Map.of("message", "해당 카테고리를 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(Map.of("message", "카테고리 삭제 성공"));
    }
}
