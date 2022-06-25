package kitchenpos.dto;

public class MenuProductRequest {
    private Long id;
    private int quantity;

    protected MenuProductRequest() {
    }

    public MenuProductRequest(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }
}
