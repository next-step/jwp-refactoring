package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

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

    public static List<MenuProduct> toEntity(List<MenuProductRequest> request) {
        return request.stream().map(MenuProductRequest::toEntity)
                .collect(Collectors.toList());
    }

    public static MenuProduct toEntity(MenuProductRequest request) {
        return new MenuProduct(request.productId, request.getQuantity());
    }

}
