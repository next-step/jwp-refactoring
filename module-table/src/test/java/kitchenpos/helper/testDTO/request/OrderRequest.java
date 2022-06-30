package kitchenpos.helper.testDTO.request;

import java.util.LinkedList;
import java.util.List;
import kitchenpos.helper.testDTO.OrderLineItemDTO;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemDTO> orderLineItems = new LinkedList<>();

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public void setOrderLineItems(List<OrderLineItemDTO> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
