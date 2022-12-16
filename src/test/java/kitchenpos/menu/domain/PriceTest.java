package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PriceTest {
    @DisplayName("메뉴 가격이 전체 상품 가격보다 클 수 없다.")
    @Test
    void 메뉴_가격이_전체_상품_가격보다_큰지_비교한다() {
        Price price = Price.of(BigDecimal.valueOf(10000L));
        assertThatIllegalArgumentException().isThrownBy(() -> price.checkValidate(BigDecimal.valueOf(5000L)));
    }
}
