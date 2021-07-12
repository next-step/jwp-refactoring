package kitchenpos.menu.dto.dto;

import kitchenpos.menu.dto.CreateMenuProductDto;

public class CreateMenuProductRequest {
    private Long productId;
    private Long quantity;

    public CreateMenuProductRequest() { }

    public CreateMenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public CreateMenuProductDto toDomainDto() {
        return new CreateMenuProductDto(productId, quantity);
    }
}
