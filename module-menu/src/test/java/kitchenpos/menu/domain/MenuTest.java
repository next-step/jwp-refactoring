package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴에 대한 단위 테스트")
class MenuTest {

    @DisplayName("메뉴에 메뉴상품을 등록하면 메뉴상품의 값이 메뉴로 매핑된다")
    @Test
    void mapping_test() {
        // given
        Product 상품 = Product.of("test", BigDecimal.valueOf(500L));
        MenuProduct 메뉴_상품 = MenuProduct.of(1L, 3);
        MenuProduct 메뉴_상품2 = MenuProduct.of(1L, 2);

        // when
        Menu 메뉴 = Menu.of("menu", BigDecimal.valueOf(500L), null, Arrays.asList(메뉴_상품, 메뉴_상품2));

        // then
        assertThat(메뉴_상품.getMenu()).isEqualTo(메뉴);
        assertThat(메뉴_상품2.getMenu()).isEqualTo(메뉴);
    }

    @DisplayName("메뉴의 이름이 null 이면 예외가 발생한다")
    @Test
    void exception_test() {
        assertThatThrownBy(() -> {
            Menu.of(null, BigDecimal.valueOf(500L), null, null);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.INVALID_NAME.getMessage());
    }

    @DisplayName("메뉴의 이름이 공백이면 예외갑 발생한다")
    @Test
    void exception_test2() {
        assertThatThrownBy(() -> {
            Menu.of("", BigDecimal.valueOf(500L), null, null);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.INVALID_NAME.getMessage());
    }
}
