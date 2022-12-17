package kitchenpos.order.domain;

import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.menu.domain.MenuTestFixture.짜장_탕수육_주문_세트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 항목 테스트")
class OrderLineItemTest {
    @Test
    @DisplayName("주문 항목 객체 생성")
    void createOrderLineItem() {
        // when
        OrderLineItem actual = OrderLineItem.of(짜장_탕수육_주문_세트, 1L);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(OrderLineItem.class)
        );
    }

    @Test
    @DisplayName("메뉴 없이 주문 항목을 생성한다.")
    void createOrderLineItemByIsEmptyMenu() {
        // when & then
        assertThatThrownBy(() -> OrderLineItem.of(null, 1L))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("메뉴는 필수입니다.");
    }

    @Test
    @DisplayName("수량이 음수인 주문 항목을 생성한다.")
    void createOrderLineItemByQuantityIsNegative() {
        // when & then
        assertThatThrownBy(() -> OrderLineItem.of(짜장_탕수육_주문_세트, -1))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("수량은 0 이상이어야 합니다.");
    }
}
