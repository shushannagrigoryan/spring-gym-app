package org.example.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginAttemptService {
    private static final int MAX_ATTEMPT = 3;
    private static final int BLOCK_TIME = 5;
    private final LoadingCache<String, Integer> attemptsCache;

    /**
     * Initializing cache for failed login attempts.
     */
    public LoginAttemptService() {
        attemptsCache = CacheBuilder.newBuilder()
            .expireAfterWrite(BLOCK_TIME, TimeUnit.MINUTES).build(new CacheLoader<>() {
                @Override
                public @NonNull Integer load(@NonNull String key) {
                    return 0;
                }
            });
    }

    /**
     * Incrementing the number of failed login attempts for the given username.
     */
    public void loginFailed(String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    /**
     * Checks if the number of failed login attempts exceeds the given threshold.
     */
    public boolean isBlocked(String username) {
        try {
            return attemptsCache.get(username) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }

}