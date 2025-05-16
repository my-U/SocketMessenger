package com.example.template.repository;

import com.example.template.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
