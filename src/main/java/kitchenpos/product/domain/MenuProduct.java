package kitchenpos.product.domain;

import kitchenpos.common.Quantity;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long menuId;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {}

    public MenuProduct(Long menuId, Product product, Quantity quantity) {
        this.menuId = menuId;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Long seq, Long menuId, Product product, Quantity quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

}
