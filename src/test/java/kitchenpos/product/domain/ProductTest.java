package kitchenpos.product.domain;

import kitchenpos.common.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @DisplayName("상품 생성시, 이름을 입력하지 않으면 예외발생")
    @Test
    void 메뉴그룹_생성_이름없으면_예외발생() {
        String 비어있는_상품이름 = null;
        BigDecimal 상품가격 = BigDecimal.valueOf(1000);

        assertThatThrownBy(() -> new Product(비어있는_상품이름, 상품가격))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_PRODUCT_NAME_REQUIRED.showText());
    }

    @DisplayName("상품 생성시, 가격을 입력하지 않으면 예외발생")
    @Test
    void 상품가격_입력되지_않은채_생성_예외발생() {
        String 상품이름 = "상품이름";
        BigDecimal 비어있는_상품가격 = null;

        assertThatThrownBy(() -> new Product(상품이름, 비어있는_상품가격))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_PRODUCT_PRICE_REQUIRED.showText());
    }

    @DisplayName("상품 생성시, 가격이 0원 미만이면 예외발생")
    @Test
    void 상품가격_0원_미만_생성_예외발생() {
        String 상품이름 = "상품이름";
        BigDecimal 음수값을_가진_상품가격 = BigDecimal.valueOf(-1000);

        assertThatThrownBy(() -> new Product(상품이름, 음수값을_가진_상품가격))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_PRODUCT_PRICE_SHOULD_BE_OVER_THAN_ZERO.showText());
    }
}
