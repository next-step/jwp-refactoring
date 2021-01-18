package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private Long id;
    private ProductResponse productResponse;
    private Long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long id, ProductResponse productResponse, Long quantity) {
        this.id = id;
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public Long getQuantity() {
        return quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getId(), ProductResponse.of(menuProduct.getProduct()), menuProduct.getQuantity());
    }
}
