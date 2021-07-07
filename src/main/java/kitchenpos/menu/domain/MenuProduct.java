package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "menu_id")
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Long menuId, Product product, Quantity quantity) {
        this.menuId = menuId;
        this.product = product;
        this.quantity = quantity;
    }

//    public MenuProduct(Long seq, Long menuId, Product product, Quantity quantity) {
//        this.seq = seq;
//        this.menuId = menuId;
//        this.product = product;
//        this.quantity = quantity;
//    }

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
