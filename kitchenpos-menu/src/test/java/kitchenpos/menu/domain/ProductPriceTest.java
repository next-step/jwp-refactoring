package kitchenpos.menu.domain;

import kitchenpos.common.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("상품 가격 단위 테스트")
public class ProductPriceTest {

    @DisplayName("가격이 동일하면 상품 가격은 동일하다.")
    @Test
    void 가격이_동일하면_상품_가격은_동일하다() {
        assertEquals(
                new ProductPrice(18000),
                new ProductPrice(18000)
        );
    }

    @DisplayName("상품 가격이 null이면 상품 가격을 생성할 수 없다.")
    @Test
    void 상품_가격이_null이면_상품_가격을_생성할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new ProductPrice(null))
                .withMessage(ErrorMessage.PRODUCT_REQUIRED_PRICE.getMessage());
    }

    @DisplayName("상품 가격이 음수면 상품 가격을 생성할 수 없다.")
    @Test
    void 상품_가격이_음수면_상품_가격을_생성할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new ProductPrice(-1))
                .withMessage(ErrorMessage.PRODUCT_INVALID_PRICE.getMessage());
    }
}
