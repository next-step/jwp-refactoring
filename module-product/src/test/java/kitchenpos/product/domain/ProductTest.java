package kitchenpos.product.domain;

import kitchenpos.product.exception.IllegalPriceException;
import kitchenpos.utils.fixture.ProductFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.utils.fixture.ProductFixtureFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("상품 도메인 테스트")
class ProductTest {
    @DisplayName("상품을 생성한다")
    @Test
    void Product_생성(){
        Product 김치찌개 = createProduct("김치찌개", 8500);
        assertAll(
                () -> assertThat(김치찌개.getName()).isEqualTo("김치찌개"),
                () -> assertThat(김치찌개.getPrice()).isEqualTo(8500)
        );
    }

    @DisplayName("상품의 가격은 0 이상이어야 한다")
    @Test
    void Product_가격_0이상_검증(){
        assertThrows(IllegalPriceException.class,
                () -> Product.of("김치찌개", -8500));
    }
}