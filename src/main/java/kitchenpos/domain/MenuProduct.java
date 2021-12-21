package kitchenpos.domain;

import kitchenpos.exception.ProductNotFoundException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, Long quantity) {
//        validate(product);
        this.product = product;
        this.quantity = Quantity.of(quantity);
    }

//    private void validate(Product product) {
//        if (Objects.isNull(product)) {
//            throw new ProductNotFoundException();
//        }
//    }

    public BigDecimal price() {
        return product.getPrice()
                .multiply(BigDecimal.valueOf(quantity.value()));
    }

    public Long getId() {
        return id;
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

    public MenuProduct by(Menu menu) {
        this.menu = menu;
        return this;
    }
}
