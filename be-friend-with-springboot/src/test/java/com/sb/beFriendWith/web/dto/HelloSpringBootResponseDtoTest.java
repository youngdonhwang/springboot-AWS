package com.sb.beFriendWith.web.dto;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloSpringBootResponseDtoTest {
    @Test
    public void 롬복_작동여부_테스트() {
        //required
        String name = "uncle_lomb";
        int amount = 35;

        //when
        HelloSpringBootResponseDto dto = new HelloSpringBootResponseDto(name, amount);

        //then
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }
}
