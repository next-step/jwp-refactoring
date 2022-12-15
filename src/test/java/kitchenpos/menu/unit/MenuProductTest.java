package kitchenpos.menu.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품 관련 단위 테스트")
public class MenuProductTest {
    @DisplayName("메뉴 상품의 가격을 계산한다.")
    @Test
    void getTotalPrice() {
        Product 햄버거 = Product.of("햄버거", BigDecimal.valueOf(5000));
        MenuProduct 햄버거_상품 = MenuProduct.of(햄버거, 2);

        assertThat(햄버거_상품.getTotalPrice()).isEqualTo(Price.of(BigDecimal.valueOf(5000*2)));
    }
}
