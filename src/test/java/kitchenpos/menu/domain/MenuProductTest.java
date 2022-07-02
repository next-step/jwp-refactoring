package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    private Product product;
    private Long quantity;

    @BeforeEach
    void setUp() {
        //given
        product = new Product("파스타", BigDecimal.valueOf(13000));
        quantity = 1L;
    }

    @DisplayName("메뉴상품을 생성한다.")
    @Test
    void create() {
        //when
        MenuProduct menuProduct = new MenuProduct(product, quantity);

        //then
        assertThat(menuProduct).isNotNull();
        assertThat(menuProduct.getProduct().getName()).isEqualTo(product.getName());
        assertThat(menuProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("수량이 0 미만인 메뉴상품은 생성에 실패한다.")
    @Test
    void create_invalidQuantity() {
        //when & then
        assertThatThrownBy(() -> new MenuProduct(product, -1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수량 0 이상이어야 합니다.");
    }

}
