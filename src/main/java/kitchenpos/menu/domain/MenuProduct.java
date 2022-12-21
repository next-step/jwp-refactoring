package kitchenpos.menu.domain;


import kitchenpos.product.domain.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Long seq, Product product, long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
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
}
