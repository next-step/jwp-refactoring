package kitchenpos.tablegroup.dto;

public class OrderTableIdRequest {
    private Long id;

    protected OrderTableIdRequest() {}

    private OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public static OrderTableIdRequest from(Long id) {
        return new OrderTableIdRequest(id);
    }

    public Long getId() {
        return id;
    }
}
