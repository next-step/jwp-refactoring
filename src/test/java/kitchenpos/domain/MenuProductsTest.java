package kitchenpos.domain;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {

    MenuProduct 메뉴상품;
    MenuProduct 메뉴상품2;

    Product 치킨;
    Product 피자;

    @Test
    @DisplayName("메뉴상품들에 포함된 메뉴와 수량으로 가격을 계산한다.")
    void getTotalPrice() {
        //given
        치킨 = new Product("치킨", new BigDecimal(15000));
        피자 = new Product("치킨", new BigDecimal(25000));
        메뉴상품 = new MenuProduct(치킨 , 2L);
        메뉴상품2 = new MenuProduct(피자 , 3L);
        MenuProducts 메뉴상품들 = new MenuProducts(Arrays.asList(메뉴상품, 메뉴상품2));

        //then
        assertThat(메뉴상품들.getTotalPrice()).isEqualTo(new BigDecimal(105000));
    }
}