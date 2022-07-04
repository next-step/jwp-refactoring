package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        //when
        Product product = new Product("피자", BigDecimal.valueOf(25000));

        //then
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo(product.getName());
        assertThat(product.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("이름이 없는 상품은 생성에 실패한다.")
    @Test
    void create_invalidName() {
        //when & then
        assertThatThrownBy(() -> new Product(null, BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품 이름이 없습니다.");
    }

    @DisplayName("가격이 0 미만인 상품은 생성에 실패한다.")
    @Test
    void create_invalidPrice() {
        //when & then
        assertThatThrownBy(() -> new Product("피자", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품의 가격은 0 이상이어야 합니다.");
    }

}
