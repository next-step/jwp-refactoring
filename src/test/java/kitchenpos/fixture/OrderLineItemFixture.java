package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderLineItemRequestDto;
import kitchenpos.dto.OrderLineItemResponseDto;
import kitchenpos.menu.domain.Menu;

public class OrderLineItemFixture {

    public static OrderLineItemRequestDto 주문항목_요청_데이터_생성(Long menuId, int quantity) {
        return new OrderLineItemRequestDto(menuId, quantity);
    }

    public static OrderLineItem 주문항목_데이터_생성(Long seq, Menu menu, long quantity) {
        return new OrderLineItem(seq, menu, quantity);
    }

    public static OrderLineItemResponseDto 주문항목_응답_데이터_생성(Long seq, Long menuId, int quantity) {
        return new OrderLineItemResponseDto(seq, menuId, quantity);
    }

}
