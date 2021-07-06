package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    @Test
    @DisplayName("상품을 생성 한다")
    public void createProduct(){
        //given
        String name = "치킨";
        BigDecimal price = new BigDecimal(20000);

        // when
        Product product = new Product(name, price);

        // then
        assertThat(product).isEqualTo(new Product(name, price));
    }

    @Test
    @DisplayName("상품을 생성 실패 - 가격이 음수")
    public void createProductFailByPriceMinus() {
        //given
        String name = "피자";
        BigDecimal price = new BigDecimal(-10000);

        //when
        //then
        assertThrows(IllegalArgumentException.class, () ->  new Product(name, price));
    }
}
