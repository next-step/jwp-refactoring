package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @DisplayName("메뉴 상품의 가격을 구할 수 있다.")
    @Test
    void totalPrice() {
        //given
        BigDecimal 상품가격 = BigDecimal.valueOf(1000);
        long 상품수량 = 5;
        MenuProduct 상품 = MenuProduct.of(Product.of("상품", 상품가격), 상품수량);
        //when
        Price price = 상품.totalPrice();
        //then
        assertThat(price).isEqualTo(Price.from(상품가격.multiply(BigDecimal.valueOf(상품수량))));
    }
}
