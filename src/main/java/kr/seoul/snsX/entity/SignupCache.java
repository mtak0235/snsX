package kr.seoul.snsX.entity;

import kr.seoul.snsX.dto.MemberSignupCacheDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignupCache {

    private Map<String, Occupant> usedEmail = new HashMap();
    private Map<String, Occupant> usedNickName = new HashMap();

    @Getter @Setter
    private class Occupant {
        private String uuid;
        private LocalDateTime expirationDate;

        public Occupant(String uuid, LocalDateTime expirationDate) {
            this.uuid = uuid;
            this.expirationDate = expirationDate;
        }
    }

    private MemberSignupCacheDto isUsable(Map<String, Occupant> cache, String key, String uuid) {
        if (!cache.containsKey(key)) {
            return new MemberSignupCacheDto(null, false);
        }
        Occupant occupant = cache.get(key);
        LocalDateTime now = LocalDateTime.now();
        if (!now.isAfter(occupant.getExpirationDate()) && !uuid.equals(occupant.getUuid())) {
            return new MemberSignupCacheDto(null, true);
        }
        occupant.setExpirationDate(now.plusDays(1L));
        if (uuid == null)
            uuid = UUID.randomUUID().toString();
        occupant.setUuid(uuid);
        return new MemberSignupCacheDto(uuid, true);
    }

    public MemberSignupCacheDto isUsableEmail(String email, String uuid) {
        return isUsable(this.usedEmail, email, uuid);
    }

    public MemberSignupCacheDto isUsableNickName(String nickName, String uuid) {
        return isUsable(this.usedNickName, nickName, uuid);
    }

    private String createCache(Map<String, Occupant> cache, String key) {
        String uuid = UUID.randomUUID().toString();
        cache.put(key, new Occupant(uuid, LocalDateTime.now().plusDays(1L)));
        return uuid;
    }

    public String createEmailCache(String email) {
        return createCache(this.usedEmail, email);
    }

    public String createNickNameCache(String nickName) {
        return createCache(this.usedNickName, nickName);
    }

    private void expireKey(Map<String, Occupant> cache, String key) {
        cache.remove(key);
    }

    public void expireEmail(String email) {
        expireKey(this.usedEmail, email);
    }

    public void expireNickName(String nickName) {
        expireKey(this.usedNickName, nickName);
    }
}
