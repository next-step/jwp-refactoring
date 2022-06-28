package kitchenpos.product.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @DisplayName("상품을 생성한다")
    @Test
    void Product_생성(){
        Product 김치찌개 = new Product("김치찌개", 8500);
        assertAll(
                () -> assertThat(김치찌개.getName()).isEqualTo("김치찌개"),
                () -> assertThat(김치찌개.getPrice()).isEqualTo(8500)
        );
    }

    @DisplayName("상품 생성 실패한다: 가격 0 미만")
    @Test
    void Product_가격_0미만_생성실패(){
        assertThrows(IllegalArgumentException.class, () -> new Product("김치찌개", -8500));
    }
}