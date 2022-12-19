package kitchenpos.tablegroup.dto;

public class OrderTableIdRequest {

    private Long id;

    public OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public OrderTableIdRequest() {
    }

    public Long getId() {
        return id;
    }
}
