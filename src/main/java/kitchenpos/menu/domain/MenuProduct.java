package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;
    @Embedded
    private Quantity quantity;

    public MenuProduct() {}

    public MenuProduct(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Long seq, Menu menu, Product product, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

}
