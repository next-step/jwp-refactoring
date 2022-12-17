package kitchenpos.table.dto;

public class OrderTableIdRequest {

    private Long id;

    private OrderTableIdRequest() {}

    public OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
