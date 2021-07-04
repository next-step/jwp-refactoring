package kitchenpos.menuproduct.dto;

import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {
    private Long seq;
    private MenuResponse menu;
    private ProductResponse product;
    private Long quantity;

    public MenuProductResponse() {}

    private MenuProductResponse(Long seq, MenuResponse menu, ProductResponse product, Long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(Long seq, MenuResponse menuResponse, ProductResponse productResponse, Long quantity) {
        return new MenuProductResponse(seq, menuResponse, productResponse, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public MenuResponse getMenu() {
        return menu;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
