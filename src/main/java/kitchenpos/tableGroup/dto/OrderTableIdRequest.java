package kitchenpos.tableGroup.dto;

public class OrderTableIdRequest {
    private Long id;

    public OrderTableIdRequest() {
    }

    public OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
