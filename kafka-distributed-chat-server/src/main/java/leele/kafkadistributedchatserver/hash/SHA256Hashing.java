package leele.kafkadistributedchatserver.hash;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SHA256Hashing {

    public String generateSHA256Hash(String param1, String param2) {
        String combinedInput = param1 + param2;

        // SHA-256 해시 생성
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String sha256Hash = encoder.encode(combinedInput);

        System.out.println("🔑SHA-256: " + sha256Hash);

        return sha256Hash;
    }
}
