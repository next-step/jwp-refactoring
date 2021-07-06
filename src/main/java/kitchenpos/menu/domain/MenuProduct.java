package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.domian.Quantity;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    public MenuProduct() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    private MenuProduct(Menu menu, Product product, Quantity quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Menu menu, Product product, Quantity quantity) {
        return new MenuProduct(menu, product, quantity);
    }

    public MenuProductResponse toResponse() {
        return MenuProductResponse.of(seq, product.toResponse(), quantity.amount());
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProductId() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
