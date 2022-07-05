package kitchenpos.domain;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    MenuProduct 메뉴상품;

    Product 치킨;

    @Test
    @DisplayName("메뉴상품에 포함된 메뉴와 수량으로 가격을 계산한다.")
    void getTotalPrice() {
        //given
        치킨 = new Product("치킨", new BigDecimal(15000));
        메뉴상품 = new MenuProduct(치킨 , 2L);

        //then
        assertThat(메뉴상품.getTotalPrice()).isEqualTo(new BigDecimal(30000));
    }
}