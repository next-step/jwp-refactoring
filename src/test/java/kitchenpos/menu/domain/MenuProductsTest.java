package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    @DisplayName("메뉴 상품들의 총 가격을 구한다")
    void sumOfProductsPrice() {
        //given
        Product 강정치킨 = new Product(1L, "강정치킨", new BigDecimal(17_000));
        MenuProduct 강정치킨메뉴상품 = new MenuProduct(강정치킨, 5);
        Product 후라이드 = new Product(1L, "후라이드", new BigDecimal(10_000));
        MenuProduct 후라이드메뉴상품 = new MenuProduct(후라이드, 3);
        MenuProducts 메뉴상품들 = new MenuProducts(null, Arrays.asList(강정치킨메뉴상품, 후라이드메뉴상품));

        //when
        Price sumOfProductsPrice = 메뉴상품들.sumOfProductsPrice();

        //then
        assertThat(sumOfProductsPrice).isEqualTo(new Price(BigDecimal.valueOf(115_000)));

    }
}