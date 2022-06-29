package kitchenpos.menu.dto;

public class MenuProductRequestDto {

    private Long productId;
    private long quantity;

    public MenuProductRequestDto() {
    }

    public MenuProductRequestDto(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
