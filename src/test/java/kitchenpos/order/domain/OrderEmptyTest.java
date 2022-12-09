package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderTableExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블이 비어있는지 여부를 관리하는 클래스 테스트")
class OrderEmptyTest {
    @Test
    void 테이블_그룹을_생성할때_비어있지_않은_주문_테이블을_포함할_수_없음() {
        OrderEmpty orderEmpty = new OrderEmpty(false);

        assertThatThrownBy(() -> {
            orderEmpty.validateForTableGrouping();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP.getMessage());
    }
}
