package kitchenpos.menu.dto;

public class MenuProductRequest {
    private final Long id;
    private final Integer quantity;

    public MenuProductRequest(final Long id, final Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
