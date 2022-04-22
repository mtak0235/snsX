package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(nativeQuery = true, value = "SELECT m.email FROM Member m WHERE m.nick_name = :nickName AND m.phone_number = :phoneNumber")
    String findMemberEmailByNickNameAndPhoneNumber(@Param("nickName") String nickName, @Param("phoneNumber") String phoneNumber);

    Member findMemberByEmail(String email);

    boolean existsMemberByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT m FROM Member m WHERE m.email=:email AND m.pw = :pw AND m.status=:status LIMIT 1")
    Member findMemberByEmailAndPwAndStatus(String email, String pw, String status);

    Member findMemberByNickName(String nickName);
}
