package kitchenpos.order.domain;

import kitchenpos.common.ErrorMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("주문 상품 단위 테스트")
class OrderLineItemTest {
    private MenuGroup 중식;
    private Menu 중식_세트;

    @BeforeEach
    void setUp() {
        중식 = new MenuGroup("중식");
        중식_세트 = new Menu("중식 세트", 26000, 중식);

        ReflectionTestUtils.setField(중식, "id", 1L);
        ReflectionTestUtils.setField(중식_세트, "id", 1L);
    }

    @DisplayName("수량이 음수이면 주문 상품을 생성할 수 없다.")
    @Test
    void 수량이_음수이면_주문_상품을_생성할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderLineItem(new Order(), 중식_세트.getId(), -1))
                .withMessage(ErrorMessage.ORDER_LINE_ITEM_INVALID_QUANTITY.getMessage());
    }
}
