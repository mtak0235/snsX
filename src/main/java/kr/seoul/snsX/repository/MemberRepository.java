package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(nativeQuery = true, value = "SELECT m.email FROM Member m WHERE m.nickName = :nickName AND m.phoneNumber = :phoneNumber")
    String findMemberEmailByNickNameAndPhoneNumber(@Param("nickName") String nickName, @Param("phoneNumber") String phoneNumber);

    boolean existsMemberByEmail(String email);

    Member findMemberByEmailAndPw(String email, String pw);

    Member findMemberByNickName(String nickName);
}
