package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(nullable = false)
    private MenuProductQuantity quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected MenuProduct() {
    }

    public MenuProduct(MenuProductQuantity quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public MenuProduct(MenuProductQuantity quantity, Menu menu, Product product) {
        this.quantity = quantity;
        this.menu = menu;
        this.product = product;
    }

    public Amount createAmount() {
        return product.createAmount(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public MenuProductQuantity getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
