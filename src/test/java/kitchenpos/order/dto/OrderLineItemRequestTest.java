package kitchenpos.order.dto;

public class OrderLineItemRequestTest {

    public static OrderLineItemRequest 주문_항목_생성_요청_객체_생성(Long menuId, Long quantity) {
        return new OrderLineItemRequest.Builder()
                .menuId(menuId)
                .quantity(quantity)
                .build();
    }
}