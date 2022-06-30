package kitchenpos.helper.testDTO.response;

public class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;


    public OrderResponse(Long id, Long orderTableId, String orderStatus) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
