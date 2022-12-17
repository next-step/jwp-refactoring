package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Menu menu;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    private long quantity;

    protected MenuProduct() {}

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        this.id = seq;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long seq, Long menuId, Long productId, long quantity) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }

    public void setMenuId(final Long menuId) {
    }

    public Long getProductId() {
        return null;
    }

    public long getQuantity() {
        return quantity;
    }

}
