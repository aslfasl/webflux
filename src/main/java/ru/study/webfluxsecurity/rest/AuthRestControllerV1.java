package ru.study.webfluxsecurity.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.study.webfluxsecurity.dto.AuthRequestDto;
import ru.study.webfluxsecurity.dto.AuthResponseDto;
import ru.study.webfluxsecurity.dto.UserDto;
import ru.study.webfluxsecurity.entity.UserEntity;
import ru.study.webfluxsecurity.mapper.UserMapper;
import ru.study.webfluxsecurity.security.CustomPrincipal;
import ru.study.webfluxsecurity.security.SecurityService;
import ru.study.webfluxsecurity.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/v1/auth")
public class AuthRestControllerV1 {

    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto dto) {
        UserEntity entity = userMapper.map(dto);
        return userService.registerUser(entity)
                .map(userMapper::map);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto) {
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(customPrincipal.getId())
                .map(userMapper::map);
    }
}
