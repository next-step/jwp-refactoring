package kitchenpos.order.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

import java.util.Arrays;

import static kitchenpos.order.fixture.OrderLineItemFixture.완료_주문_항목;
import static kitchenpos.order.fixture.OrderLineItemFixture.주문_항목;
import static kitchenpos.table.fixture.TableFixture.비어있는_주문_테이블_그룹_없음;

public class OrderFixture {

    public static Order 요리중_상태_주문 = create(
            1L,
            비어있는_주문_테이블_그룹_없음,
            OrderStatus.COOKING,
            OrderLineItems.of(Arrays.asList(주문_항목))
    );

    public static Order 주문완료_상태_주문 = create(
            2L,
            비어있는_주문_테이블_그룹_없음,
            OrderStatus.COMPLETION,
            OrderLineItems.of(Arrays.asList(완료_주문_항목))
    );

    public static Order create(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        return Order.of(id, orderTable, orderStatus, orderLineItems);
    }
}
