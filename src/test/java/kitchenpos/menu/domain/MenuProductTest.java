package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MenuProductTest {

    @Test
    @DisplayName("MenuProduct가 정상적으로 생성된다.")
    void createMenuProduct() {
        MenuProduct menuProduct = new MenuProduct(new Product("상품", BigDecimal.TEN), 10);

        assertThat(menuProduct.getProduct()).isNotNull();
        assertThat(menuProduct.getQuantity()).isEqualTo(10);
    }

    @ParameterizedTest(name = "{0}, {1} -> {2}")
    @DisplayName("MenuProduct 생성시 유효성 검사를 체크한다.")
    @MethodSource("providerCreateMenuProductFailCase")
    void createFail(Product product, long quantity, Class<? extends Exception> exception) {
        assertThatExceptionOfType(exception)
            .isThrownBy(() -> new MenuProduct(product, quantity));
    }

    private static Stream<Arguments> providerCreateMenuProductFailCase() {
        return Stream.of(
            Arguments.of(null, 10L, NullPointerException.class),
            Arguments.of(new Product("상품", BigDecimal.TEN), -1, IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("메뉴 상품의 가격을 구한다.")
    void totalPrice() {
        MenuProduct menuProduct = new MenuProduct(new Product("상품", BigDecimal.TEN), 10);

        assertThat(menuProduct.totalPrice()).isEqualTo(BigDecimal.valueOf(100));
    }

}
