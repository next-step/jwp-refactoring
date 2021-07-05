package kitchenpos.menu.dto;

import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {
    private Long seq;
    private ProductResponse product;
    private Long quantity;

    public MenuProductResponse() {}

    private MenuProductResponse(Long seq, ProductResponse product, Long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(Long seq, ProductResponse productResponse, Long quantity) {
        return new MenuProductResponse(seq, productResponse, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
