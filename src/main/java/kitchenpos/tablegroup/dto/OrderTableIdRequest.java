package kitchenpos.tablegroup.dto;

public class OrderTableIdRequest {
    private long id;

    protected OrderTableIdRequest() {
    }

    private OrderTableIdRequest(long id) {
        this.id = id;
    }

    public static OrderTableIdRequest of(long id) {
        return new OrderTableIdRequest(id);
    }

    public long getId() {
        return id;
    }
}
