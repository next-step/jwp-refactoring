package kitchenpos.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "MENU_PRODUCT_SEQ_GENERATOR",
        sequenceName = "MENU_PRODUCT_SEQ",
        initialValue = 1,
        allocationSize = 1)
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MENU_PRODUCT_SEQ_GENERATOR")
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menuId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;
    private long quantity;

    public MenuProduct() {
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getMenuId() {
        return menuId.getId();
    }

    public void setMenuId(final Long menuId) {
        this.menuId.setId(menuId);
    }

    public Long getProductId() {
        return productId.getId();
    }

    public void setProductId(final Long productId) {
        this.productId.setId(productId);
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
