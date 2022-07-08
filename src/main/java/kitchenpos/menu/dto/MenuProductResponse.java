package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private final Long productId;
    private final long quantity;

    public MenuProductResponse(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public static List<MenuProductResponse> ofList(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "MenuProductResponse{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductResponse that = (MenuProductResponse) o;
        return quantity == that.quantity && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }
}
