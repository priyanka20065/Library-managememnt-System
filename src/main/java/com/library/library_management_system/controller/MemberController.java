package com.library.library_management_system.controller;

import com.library.library_management_system.entity.Member;
import com.library.library_management_system.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Member member) {
        boolean updated = memberService.updateProfile(id, member.getUsername(), member.getEmail());
        return updated ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Profile update failed");
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        boolean changed = memberService.changePassword(id, oldPassword, newPassword);
        return changed ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Password change failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
