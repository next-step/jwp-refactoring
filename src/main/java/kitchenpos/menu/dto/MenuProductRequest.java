package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    public static List<MenuProduct> of(List<MenuProductRequest> request) {
        return request.stream().map(MenuProductRequest::of)
                .collect(Collectors.toList());
    }

    public static MenuProduct of(MenuProductRequest request) {
        return new MenuProduct(request.getProductId(), request.getQuantity());
    }

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, long quantity) {
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
