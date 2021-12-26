package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @DisplayName("메뉴 상품 생성")
    @Test
    void construct() {
        Product 타코야끼 = new Product(1L, "타코야끼", Price.valueOf(BigDecimal.valueOf(12000)));
        MenuProduct menuProduct = new MenuProduct(타코야끼.getId(), new Quantity(2));
        MenuProduct expectedMenuProduct = new MenuProduct(타코야끼.getId(), new Quantity(2));
        assertThat(menuProduct.getProductId()).isEqualTo(expectedMenuProduct.getProductId());
    }
}
