package com.library.library_management_system.service;

import com.library.library_management_system.entity.Member;
import com.library.library_management_system.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> getByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member saveMember(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        // Default role assignment if not set
        if (member.getRole() == null) {
            member.setRole(Member.Role.MEMBER);
        }
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public boolean updateProfile(Long id, String username, String email) {
        Optional<Member> memberOpt = memberRepository.findById(id);
        if (memberOpt.isEmpty()) return false;
        Member member = memberOpt.get();
        member.setUsername(username);
        member.setEmail(email);
        memberRepository.save(member);
        return true;
    }

    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        Optional<Member> memberOpt = memberRepository.findById(id);
        if (memberOpt.isEmpty()) return false;
        Member member = memberOpt.get();
        if (!passwordEncoder.matches(oldPassword, member.getPassword())) return false;
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        return true;
    }
}
