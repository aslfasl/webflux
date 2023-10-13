package ru.study.webfluxsecurity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.study.webfluxsecurity.entity.UserEntity;
import ru.study.webfluxsecurity.entity.UserRole;
import ru.study.webfluxsecurity.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public Mono<UserEntity> registerUser(UserEntity userEntity) {
        return userRepository.save(userEntity.toBuilder()
                .password(encoder.encode(userEntity.getPassword()))
                        .role(UserRole.USER)
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                .build()
        ).doOnSuccess(user -> log.info("In registerUser - user: {} create", user));
    }

    public Mono<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
