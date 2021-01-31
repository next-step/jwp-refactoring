package kitchenpos.menu;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductTest {

    @DisplayName("생성 테스트")
    @Test
    public void createTest() {
        Product product1 = new Product("상품1", BigDecimal.valueOf(1000));
        MenuProduct menuProduct = new MenuProduct(product1, 10);
        assertThat(menuProduct.getTotalPrice()).isEqualTo(BigDecimal.valueOf(10000));
    }

}
