package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 상품_생성() {
        //when
        Product 치킨 = new Product("치킨", new BigDecimal(100L));

        //then
        assertThat(치킨.getName()).isEqualTo("치킨");
        assertThat(치킨.getPrice()).isEqualTo(new BigDecimal(100L));
    }

    @Test
    void 상품명이_없으면_오류_발생() {
        //when & then
        Assertions.assertThatThrownBy(() -> new Product(null, new BigDecimal(100L)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품명이 없습니다");
    }

    @Test
    void 상품_가격이_없으면_안됨() {
        //when & then
        assertThatThrownBy(() -> new Product("치킨", null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격정보가 없거나 음수 입니다.");
    }

    @Test
    void 상품_가격이_음수면_안됨() {
        //when & then
        assertThatThrownBy(() -> new Product("치킨", new BigDecimal(-1)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격정보가 없거나 음수 입니다.");
    }
}