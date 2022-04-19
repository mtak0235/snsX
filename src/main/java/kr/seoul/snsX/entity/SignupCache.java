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

    private Map<String, Occupant> setCache(boolean flag) {
        if (!flag)
            return this.usedEmail;
        return this.usedNickName;
    }

    public MemberSignupCacheDto isUsable(String key, String uuid, boolean flag) {
        Map<String, Occupant> cache = setCache(flag);
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

    public String createCache(String key, boolean flag) {
        Map<String, Occupant> cache = setCache(flag);
        String uuid = UUID.randomUUID().toString();
        cache.put(key, new Occupant(uuid, LocalDateTime.now().plusDays(1L)));
        return uuid;
    }

    public void expireKey(String key, boolean flag) {
        Map<String, Occupant> cache = setCache(flag);
        cache.remove(key);
    }
}
