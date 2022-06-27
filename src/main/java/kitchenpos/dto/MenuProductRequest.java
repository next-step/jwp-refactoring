package kitchenpos.dto;

public class MenuProductRequest {
    private Long id;
    private Long quantity;

    protected MenuProductRequest() {
    }

    public MenuProductRequest(final Long id, final long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }
}
