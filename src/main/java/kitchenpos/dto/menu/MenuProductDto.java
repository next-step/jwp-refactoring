package kitchenpos.dto.menu;

import java.util.Objects;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductDto {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    protected MenuProductDto() {
    }

    private MenuProductDto(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.productId = productId;
    }

    public static MenuProductDto of(Long seq, Long menuId, Long productId, long quantity) {
        return new MenuProductDto(seq, menuId, productId, quantity);
    }

    public static MenuProductDto of(Long productId, long quantity) {
        return new MenuProductDto(null, null, productId, quantity);
    }

    public static MenuProductDto of(MenuProduct menuProduct) {
        if (menuProduct.getMenu() == null) {
            return new MenuProductDto(menuProduct.getSeq(), null, menuProduct.getProduct().getId(), menuProduct.getQuantity());
        }

        return new MenuProductDto(menuProduct.getSeq(), menuProduct.getMenu().getId(), menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }
    
    public Long getSeq() {
        return this.seq;
    }

    public Long getMenuId() {
        return this.menuId;
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
        return Objects.equals(seq, menuProductDto.seq) && Objects.equals(menuId, menuProductDto.menuId) && Objects.equals(productId, menuProductDto.productId) && quantity == menuProductDto.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, productId, quantity);
    }
}
