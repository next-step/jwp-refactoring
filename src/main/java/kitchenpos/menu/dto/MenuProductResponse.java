package kitchenpos.menu.dto;

import java.util.Objects;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {

    private Long id;
    private ProductResponse product;
    private long quantity;

    private MenuProductResponse() {
    }

    private MenuProductResponse(Long seq, ProductResponse product, long quantity) {
        this.id = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getId(),
            ProductResponse.from(menuProduct.getProduct()),
            menuProduct.getQuantityVal());
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProduct() {
        return product;
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
            && Objects.equals(getProduct(), that.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProduct(), getQuantity());
    }
}
