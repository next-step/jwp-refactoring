package kitchenpos.dto;

public class OrderTableIdRequest {
    private Long id;

    private OrderTableIdRequest() {
    }

    private OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static OrderTableIdRequest from(Long id) {
        return new OrderTableIdRequest(id);
    }
}
