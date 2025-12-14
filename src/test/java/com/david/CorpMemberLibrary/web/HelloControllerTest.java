package com.david.CorpMemberLibrary.web;

import com.david.CorpMemberLibrary.web.dto.HelloResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("HelloController 테스트")
class HelloControllerTest {

    private HelloController helloController = new HelloController();

    @Test
    @DisplayName("/hello 엔드포인트가 'hello'를 반환하는지 테스트")
    public void testHello() {
        //when
        String result = helloController.hello();

        //then
        assertThat(result).isEqualTo("hello");
    }

    @Test
    @DisplayName("/hello/dto 엔드포인트가 HelloResponseDto를 반환하는지 테스트")
    public void testHelloDto() {
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto result = helloController.helloDto(name, amount);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getAmount()).isEqualTo(amount);
    }
}