package com.library.library_management_system.service;
import com.library.library_management_system.entity.Member;
import com.library.library_management_system.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public Optional<Member> authenticate(String username, String password) {
        Optional<Member> memberOpt = memberRepository.findByUsername(username);
        if (memberOpt.isPresent() && passwordEncoder.matches(password, memberOpt.get().getPassword())) {
            return memberOpt;
        }
        return Optional.empty();
    }
    public boolean register(Member member) {
        if (memberRepository.findByUsername(member.getUsername()).isPresent() ||
            memberRepository.findByEmail(member.getEmail()).isPresent() ||
            (member.getMemberId() != null && memberRepository.findAll().stream().anyMatch(m -> member.getMemberId().equals(m.getMemberId())))) {
            return false; 
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        if (member.getRole() == null) {
            member.setRole(Member.Role.MEMBER);
        }
        memberRepository.save(member);
        return true;
    }
}
