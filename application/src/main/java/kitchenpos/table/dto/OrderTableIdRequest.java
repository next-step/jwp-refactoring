package kitchenpos.table.dto;

public class OrderTableIdRequest {

    private Long id;

    public OrderTableIdRequest() { }

    public OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public OrderTableIdDto toDomainDto() {
        return new OrderTableIdDto(id);
    }

    public Long getId() {
        return id;
    }
}
