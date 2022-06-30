package kitchenpos.product.domain;

import kitchenpos.exception.IllegalPriceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("상품 도메인 테스트")
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

    @DisplayName("상품의 가격은 0 이상이어야 한다")
    @Test
    void Product_가격_0이상_검증(){
        assertThrows(IllegalPriceException.class,
                () -> new Product("김치찌개", -8500));
    }
}