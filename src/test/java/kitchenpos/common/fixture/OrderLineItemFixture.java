package kitchenpos.common.fixture;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequestDto;
import kitchenpos.order.dto.OrderLineItemResponseDto;

public class OrderLineItemFixture {

    public static OrderLineItemRequestDto 주문항목_요청_데이터_생성(Long menuId, int quantity) {
        return new OrderLineItemRequestDto(menuId, quantity);
    }

    public static OrderLineItem 주문항목_데이터_생성(Long seq, Long menuId, long quantity) {
        return new OrderLineItem(seq, menuId, quantity);
    }

    public static OrderLineItemResponseDto 주문항목_응답_데이터_생성(Long seq, Long menuId, int quantity) {
        return new OrderLineItemResponseDto(seq, menuId, quantity);
    }

}
