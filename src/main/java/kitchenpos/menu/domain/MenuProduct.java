package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    public MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Long id, Long seq, Product product, long quantity, Menu menu) {
        this.id = id;
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
        this.menu = menu;
    }

    public Long getId() {
        return id;
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
