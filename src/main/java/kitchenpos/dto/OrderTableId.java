package kitchenpos.dto;

public class OrderTableId {
    private Long id;

    protected OrderTableId() {}

    public OrderTableId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
