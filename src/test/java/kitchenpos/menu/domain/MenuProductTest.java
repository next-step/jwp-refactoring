package kitchenpos.menu.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {

    @DisplayName("메뉴 상품을 등록할 수 있다.")
    @Test
    void create_menu() {

        // given
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        Menu menu = new Menu("menu", BigDecimal.valueOf(1000), menuGroup);
        Product product = new Product("product", BigDecimal.valueOf(500));
        // when
        MenuProduct savedMenuProduct = new MenuProduct(menu, product, 1);

        // then
        Assertions.assertAll(
                () -> assertThat(savedMenuProduct.getQuantity()).isEqualTo(1),
                () -> assertThat(savedMenuProduct.getMenu()).isEqualTo(menu),
                () -> assertThat(savedMenuProduct.getProduct()).isEqualTo(product)
        );
    }

    @DisplayName("메뉴 상품 정보가 올바르지 않으면 등록할 수 없다.")
    @ParameterizedTest()
    @MethodSource("invalid_menus")
    void validate_menu(Menu menu, Product product, long quantity) {
        // then
        assertThatThrownBy(() -> new MenuProduct(menu, product, quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalid_menus() {
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        return Stream.of(
                Arguments.of(null, new Product("product", BigDecimal.valueOf(500)), 2),
                Arguments.of(new Menu("menu", BigDecimal.valueOf(10000), menuGroup), null, 3),
                Arguments.of(new Menu("menu", BigDecimal.valueOf(1000000), menuGroup), null, 0),
                Arguments.of(null, null, 1)
        );
    }

}
