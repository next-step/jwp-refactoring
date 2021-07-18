package kitchenpos.order;

import kitchenpos.menu.MenuTestFixture;
import kitchenpos.order.dto.*;
import kitchenpos.ordertable.OrderTable;

import java.util.Arrays;

import static kitchenpos.order.OrderStatus.COOKING;


public class OrderTableTestFixture {

    public static final Long 단체지정_안됨 = null;
    public static final OrderTable 비어있지_않은_테이블 = new OrderTable(1L, 단체지정_안됨, 3, false);
    public static final OrderTable 비어있는_테이블 = new OrderTable(2L, 단체지정_안됨, 3, true);

    public static final Order 첫번째_주문 = new Order(1L, 비어있지_않은_테이블.getId(), COOKING);
    public static final Order 두번째_주문 = new Order(2L, 비어있지_않은_테이블.getId(), COOKING);

    public static final OrderTableRequest 비어있지_않은_테이블_요청 = new OrderTableRequest(비어있지_않은_테이블.getNumberOfGuests(), 비어있지_않은_테이블.isEmpty());
    public static final OrderTableRequest 비어있는_테이블_요청 = new OrderTableRequest(비어있는_테이블.getNumberOfGuests(), 비어있는_테이블.isEmpty());

    public static final OrderTableResponse 비어있지_않은_테이블_응답 = new OrderTableResponse(비어있지_않은_테이블.getId(), 비어있지_않은_테이블.getTableGroupId(),
            비어있지_않은_테이블.getNumberOfGuests(), 비어있지_않은_테이블.isEmpty());
    public static final OrderTableResponse 비어있는_테이블_응답 = new OrderTableResponse(비어있는_테이블.getId(), 비어있는_테이블.getTableGroupId(),
            비어있는_테이블.getNumberOfGuests(), 비어있는_테이블.isEmpty());

    public static final OrderLineItemRequest 주문_항목_첫번째_요청 = new OrderLineItemRequest(MenuTestFixture.맥모닝콤보.getId(), 1L);
    public static final OrderLineItemRequest 주문_항목_두번째_요청 = new OrderLineItemRequest(MenuTestFixture.맥모닝콤보.getId(), 3L);
    public static final OrderRequest 첫번째_주문_요청 = new OrderRequest(비어있지_않은_테이블.getId(), Arrays.asList(주문_항목_첫번째_요청, 주문_항목_두번째_요청));

    public static final OrderLineItemResponse 주문_항목_첫번째_응답 = new OrderLineItemResponse(1L, 첫번째_주문.getId(), MenuTestFixture.맥모닝콤보.getId(), 1L);
    public static final OrderLineItemResponse 주문_항목_두번째_응답 = new OrderLineItemResponse(2L, 첫번째_주문.getId(), MenuTestFixture.맥모닝콤보.getId(), 3L);
    public static final OrderResponse 첫번째_주문_응답
            = new OrderResponse(첫번째_주문.getId(), 첫번째_주문.getOrderTableId(), 첫번째_주문.getOrderStatus().name(),
            첫번째_주문.getOrderedTime(), Arrays.asList(주문_항목_첫번째_응답, 주문_항목_두번째_응답));
    public static final OrderResponse 두번째_주문_응답
            = new OrderResponse(두번째_주문.getId(), 두번째_주문.getOrderTableId(), 두번째_주문.getOrderStatus().name(),
            두번째_주문.getOrderedTime(), Arrays.asList(주문_항목_첫번째_응답, 주문_항목_두번째_응답));
}
