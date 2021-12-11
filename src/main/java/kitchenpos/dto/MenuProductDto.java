package kitchenpos.dto;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;

public class MenuProductDto {
    private Long seq;
    private Menu menu;
    private Long productId;
    private long quantity;

    protected MenuProductDto() {
    }

    private MenuProductDto(Long seq, Menu menu, Product product, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = product.getId();
        this.quantity = quantity;
    }

    public static MenuProductDto of(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProductDto(seq, menu, product, quantity);
    }

    public static MenuProductDto of(MenuProduct menuProduct) {
        return new MenuProductDto(menuProduct.getSeq(), menuProduct.getMenu(), menuProduct.getProduct(), menuProduct.getQuantity());
    }
    
    public Long getSeq() {
        return this.seq;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Long getProductId() {
        return this.productId;
    }

    public long getQuantity() {
        return this.quantity;
    }
}
