package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.product.domain.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 테스트")
public class MenuProductTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        final Product 상품 = 상품("상품");
        final long 수량 = 1;
        assertThat(MenuProduct.of(상품, 수량)).isEqualTo(MenuProduct.of(상품, 수량));
    }

    public static MenuProduct 메뉴_상품(Product product, long quantity) {
        return MenuProduct.of(product, quantity);
    }
}
