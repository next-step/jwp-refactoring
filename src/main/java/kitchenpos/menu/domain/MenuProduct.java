package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "MENU_ID", nullable = false)
    private Long menuId;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct(){

    }

    public MenuProduct(Product product, long quantity){
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

    public long getQuantity() {
        return quantity;
    }
}
