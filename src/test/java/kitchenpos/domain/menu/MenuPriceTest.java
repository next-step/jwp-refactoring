package kitchenpos.domain.menu;

import kitchenpos.domain.menu.exceptions.InvalidMenuPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuPriceTest {
    @DisplayName("올바르지 않은 메뉴 가격을 생성할 수 없다.")
    @ParameterizedTest
    @NullSource
    @MethodSource("menuCreateFailByInvalidPriceResource")
    void menuCreateFailByInvalidPrice(BigDecimal invalidValue) {
        assertThatThrownBy(() -> new MenuPrice(invalidValue))
                .isInstanceOf(InvalidMenuPriceException.class)
                .hasMessage("가격은 음수일 수 없습니다.");
    }
    public static Stream<Arguments> menuCreateFailByInvalidPriceResource() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of(BigDecimal.valueOf(-2))
        );
    }
}