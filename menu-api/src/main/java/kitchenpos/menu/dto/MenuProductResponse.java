package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

import java.util.Objects;

public class MenuProductResponse {
    private final Long seq;
    private final ProductResponse productResponse;
    private final long quantity;

    public MenuProductResponse(final Long seq, final ProductResponse productResponse, final long quantity) {
        this.seq = seq;
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(), ProductResponse.from(menuProduct.getProduct()), menuProduct.getQuantity()
        );
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuProductResponse that = (MenuProductResponse) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
