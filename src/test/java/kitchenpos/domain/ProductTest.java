package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("상품 생성")
    public void createProductTest() {
        //when
        Product product = new Product("떡볶이", new Price(BigDecimal.valueOf(15000)));

        //then
        assertThat(product).isNotNull();
    }

    @Test
    @DisplayName("상품 가격 정보가 없으면 생성 실패")
    public void createProductFailPriceIsNullTest() {
        //when
        assertThatThrownBy(() -> new Product("떡볶이", new Price((null))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 가격이 0보다 작으면 생성 실패")
    public void createProductFailPriceLessThanZeroTest() {
        //when
        assertThatThrownBy(() -> new Product("떡볶이", new Price(BigDecimal.valueOf(-1000))))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
