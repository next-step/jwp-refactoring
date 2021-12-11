package kitchenpos.dto;

import java.util.Objects;

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
        this.quantity = quantity;
        
        if (product == null) {
            this.productId = null;
            return;
        }

        this.productId = product.getId();
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MenuProductDto)) {
            return false;
        }
        MenuProductDto menuProductDto = (MenuProductDto) o;
        return Objects.equals(seq, menuProductDto.seq) && Objects.equals(menu, menuProductDto.menu) && Objects.equals(productId, menuProductDto.productId) && quantity == menuProductDto.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menu, productId, quantity);
    }
}
