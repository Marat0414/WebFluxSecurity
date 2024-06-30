package net.maratm.webfluxsecurity.rest;

import lombok.RequiredArgsConstructor;
import net.maratm.webfluxsecurity.dto.AuthRequestDto;
import net.maratm.webfluxsecurity.dto.AuthResponseDto;
import net.maratm.webfluxsecurity.dto.UserDto;
import net.maratm.webfluxsecurity.entity.UserEntity;
import net.maratm.webfluxsecurity.mapper.UserMapper;
import net.maratm.webfluxsecurity.security.CustomPrincipal;
import net.maratm.webfluxsecurity.security.SecurityService;
import net.maratm.webfluxsecurity.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
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
                .flatMap(tokenDetails -> Mono.just(AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                        )
                );
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(customPrincipal.getId())
                .map(userMapper::map);
    }


}
