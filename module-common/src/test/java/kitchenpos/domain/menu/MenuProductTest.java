package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MenuProductTest {

    @Test
    @DisplayName("MenuProduct가 정상적으로 생성된다.")
    void createMenuProduct() {
        MenuProduct menuProduct = new MenuProduct(10L, 10);

        assertThat(menuProduct.getProductId()).isNotNull();
        assertThat(menuProduct.getQuantity()).isEqualTo(10);
    }

    @ParameterizedTest(name = "{0}, {1} -> {2}")
    @DisplayName("MenuProduct 생성시 유효성 검사를 체크한다.")
    @MethodSource("providerCreateMenuProductFailCase")
    void createFail(Long productId, long quantity, Class<? extends Exception> exception) {
        assertThatExceptionOfType(exception)
            .isThrownBy(() -> new MenuProduct(productId, quantity));
    }

    private static Stream<Arguments> providerCreateMenuProductFailCase() {
        return Stream.of(
            Arguments.of(null, 10L, NullPointerException.class),
            Arguments.of(1L, -1, IllegalArgumentException.class)
        );
    }

}
