package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

@DisplayName("MenuPrice 테스트")
class MenuPriceTest {

    @Test
    @DisplayName("객체 기본 생성 확인")
    void create() {
        // when
        MenuPrice menuPrice = new MenuPrice(BigDecimal.valueOf(10));

        // then
        assertThat(menuPrice).isNotNull();
    }

    @TestFactory
    @DisplayName("금액 생성 시도 시 오류 발생")
    List<DynamicTest> price_exception() {
        return Arrays.asList(
                dynamicTest("Null값 생성 시도시 오류 발생", () -> assertThatThrownBy(() -> new MenuPrice(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("금액은 Null이거나 0보다 작을 수 없습니다.")),
                dynamicTest("음수값 생성 시도시 오류 발생", () -> assertThatThrownBy(() -> new MenuPrice(BigDecimal.valueOf(-1)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("금액은 Null이거나 0보다 작을 수 없습니다."))
        );
    }
}
