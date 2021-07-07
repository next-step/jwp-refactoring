package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_MenuProduct_to_menu_id"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_MenuProduct_to_product_id"))
    private Product product;

    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(final Menu menu, final Product product, final long quantity) {
        this(null, menu, product, quantity);
    }

    public MenuProduct(final Long id, final Menu menu, final Product product, final long quantity) {
        setMenu(menu);

        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

    private void setMenu(final Menu menu) {
        this.menu = menu;
        menu.appendMenuProducts(this);
    }
}
