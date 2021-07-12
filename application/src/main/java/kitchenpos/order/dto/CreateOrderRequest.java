package kitchenpos.order.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static java.util.stream.Collectors.toList;

public class CreateOrderRequest {

    private Long orderTableId;

    @NotNull
    @Size(min = 1, message = "주문 시 주문 항목은 1개 이상 포함되어야 합니다.")
    private List<CreateOrderLineItemRequest> orderLineItems;

    public CreateOrderRequest() { }

    public CreateOrderRequest(Long orderTableId, List<CreateOrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public CreateOrderDto toDomainDto() {
        return new CreateOrderDto(orderTableId, orderLineItems.stream()
                                                              .map(CreateOrderLineItemRequest::toDomainDto)
                                                              .collect(toList()));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<CreateOrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
