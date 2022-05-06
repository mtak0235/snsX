package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Member;
import kr.seoul.snsX.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(nativeQuery = true, value = "SELECT m.email FROM Member m WHERE m.nick_name = :nickName AND m.phone_number = :phoneNumber")
    String findMemberEmailByNickNameAndPhoneNumber(@Param("nickName") String nickName, @Param("phoneNumber") String phoneNumber);

    Member findMemberByEmail(String email);

    boolean existsMemberByEmail(String email);

    Member findTop1ByEmailAndPwAndStatus(String email,String pw, Status status);

    Member findMemberByNickName(String nickName);

    Member findMemberByIdAndPw(Long memberId, String pw);
}
