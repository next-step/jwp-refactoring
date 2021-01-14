package kitchenpos.table.dto;

public class OrderTableIdRequest {
    private long id;

    public OrderTableIdRequest() {
    }

    public OrderTableIdRequest(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
