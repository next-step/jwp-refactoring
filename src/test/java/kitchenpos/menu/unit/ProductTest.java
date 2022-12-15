package kitchenpos.menu.unit;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 도메인 테스트")
public class ProductTest {

    @DisplayName("상품을 정상 생성한다.")
    @Test
    void create_success() {
        // when
        String 상품_이름 = "짜장면";
        BigDecimal 가격 = BigDecimal.valueOf(10_000);
        Product 짜장면 = Product.of(상품_이름, 가격);
        // then
        assertAll(
                ()-> assertThat(짜장면.getName()).isEqualTo(Name.of(상품_이름)),
                ()-> assertThat(짜장면.getPrice()).isEqualTo(Price.of(가격))
        );
    }

}
