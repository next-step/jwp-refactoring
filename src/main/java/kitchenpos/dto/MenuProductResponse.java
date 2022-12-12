package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.util.Objects;

public class MenuProductResponse {
    private final Long seq;
    private final Menu menu;
    private final Product product;
    private final long quantity;

    public MenuProductResponse(final Long seq, final Menu menu, final Product product, final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(), menuProduct.getMenu(), menuProduct.getProduct(), menuProduct.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
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
