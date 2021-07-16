package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    private MenuProducts menuProducts;

    @BeforeEach
    void setUp() {
        menuProducts = new MenuProducts();
        MenuProduct 테스트_상품_1 = new MenuProduct(1L, 3);
        MenuProduct 테스트_상품_2 = new MenuProduct(2L, 1);
        menuProducts.add(Arrays.asList(테스트_상품_1, 테스트_상품_2));
    }

    @DisplayName("제네릭을 사용한 변환 테스트")
    @Test
    void convertAllTest() {
        // when
        assertThat(menuProducts.convertAll(menuProduct -> menuProduct.getProductId()))
            .hasSize(2)
            .containsExactly(1L, 2L);
    }

}
