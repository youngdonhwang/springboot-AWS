package com.sb.beFriendWith.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class HelloSpringBootResponseDto {

    private final String name;
    private final int amount;
}
