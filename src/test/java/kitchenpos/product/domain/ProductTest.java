package kitchenpos.product.domain;

import kitchenpos.common.Message;
import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @DisplayName("상품 생성시, 이름을 입력하지 않으면 예외발생")
    @Test
    void 메뉴그룹_생성_이름없으면_예외발생() {
        String 비어있는_상품이름 = null;
        Price 상품가격 = Price.valueOf(1000);

        assertThatThrownBy(() -> new Product(비어있는_상품이름, 상품가격))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_PRODUCT_NAME_REQUIRED.showText());
    }
}
