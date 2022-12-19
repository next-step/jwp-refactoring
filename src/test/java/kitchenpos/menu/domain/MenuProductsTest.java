package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {



    @DisplayName("메뉴 상품들의 모든 가격 총합을 구할 수 있다.")
    @Test
    void totalPrice() {
        //given
        long 강장치킨수량 = 2;
        BigDecimal 강장치킨가격 = BigDecimal.valueOf(1000);
        Product 강장치킨 = Product.of("강장치킨", 강장치킨가격);

        long 양념치킨수량 = 3;
        BigDecimal 양념치킨가격 = BigDecimal.valueOf(2000);
        Product 양념치킨 = Product.of("양념치킨", 양념치킨가격);

        MenuProduct 강장치킨상품 = MenuProduct.of(강장치킨, 강장치킨수량);
        MenuProduct 양념치킨상품 = MenuProduct.of(양념치킨, 양념치킨수량);

        MenuProducts menuProducts = new MenuProducts();
        Arrays.asList(강장치킨상품, 양념치킨상품).forEach(menuProducts::add);

        //when
        Price price = menuProducts.totalPrice();

        //then
        Assertions.assertThat(price).isEqualTo(Price.from(BigDecimal.valueOf(8000)));

    }
}
