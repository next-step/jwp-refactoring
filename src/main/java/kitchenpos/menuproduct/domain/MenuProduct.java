package kitchenpos.menuproduct.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.domian.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menuproduct.dto.MenuProductResponse;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

    public MenuProduct() {}

    private MenuProduct(Menu menu, Product product, Quantity quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    public static MenuProduct of(Menu menu, Product product, Quantity quantity) {
        return new MenuProduct(menu, product, quantity);
    }

    public MenuProductResponse toResponse() {
        return MenuProductResponse.of(seq, menu.toResponse(), product.toResponse(), quantity.amount());
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
