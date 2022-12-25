package kitchenpos.product.unit;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.product.domain.Product;

@DisplayName("상품 도메인 테스트")
public class ProductTest {
    @DisplayName("상품을 정상 생성한다.")
    @Test
    void create_success() {
        // given
        String 상품_이름 = "짜장면";
        BigDecimal 가격 = BigDecimal.valueOf(10_000);
        // when
        Product 짜장면 = Product.of(상품_이름, 가격);
        // then
        assertAll(
            () -> assertThat(짜장면.getName()).isEqualTo(Name.of(상품_이름)),
            () -> assertThat(짜장면.getPrice()).isEqualTo(Price.of(가격))
        );
    }
}
