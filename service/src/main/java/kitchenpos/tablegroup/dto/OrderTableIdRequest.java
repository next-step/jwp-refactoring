package kitchenpos.tablegroup.dto;

public class OrderTableIdRequest {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderTableIdRequest() {
    }

    public OrderTableIdRequest(Long id) {
        this.id = id;
    }
}
