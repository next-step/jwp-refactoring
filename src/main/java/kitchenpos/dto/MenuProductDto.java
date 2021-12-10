package kitchenpos.dto;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Product;

public class MenuProductDto {
    private Long seq;
    private Menu menu;
    private Product product;
    private long quantity;

    protected MenuProductDto() {
    }

    private MenuProductDto(Long seq, Menu menu, Product product, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductDto of(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProductDto(seq, menu, product, quantity);
    }

    public Long getSeq() {
        return this.seq;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Product getProduct() {
        return this.product;
    }

    public long getQuantity() {
        return this.quantity;
    }
}
