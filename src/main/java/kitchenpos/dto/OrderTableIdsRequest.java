package kitchenpos.dto;

public class OrderTableIdsRequest {
    private Long id;

    public OrderTableIdsRequest() {
    }

    public OrderTableIdsRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
