package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import java.util.Objects;

public class MenuProductResponse {

    private Long id;
    private Long productId;
    private long quantity;

    private MenuProductResponse() {
    }

    public MenuProductResponse(Long id, Long productId, long quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getId(), menuProduct.getProductId(),
            menuProduct.getQuantityVal());
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProductResponse that = (MenuProductResponse) o;
        return getQuantity() == that.getQuantity() && Objects.equals(getId(), that.getId())
            && Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProductId(), getQuantity());
    }
}
