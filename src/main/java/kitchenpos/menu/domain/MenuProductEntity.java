package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "menu_product")
public class MenuProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private MenuEntity menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private long quantity;

    protected MenuProductEntity() {
    }

    private MenuProductEntity(MenuEntity menu, ProductEntity product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductEntity of(MenuEntity menu, ProductEntity product, long quantity) {
        return new MenuProductEntity(menu, product, quantity);
    }

    public void validateHasProduct() {
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException();
        }
    }

    public Price getTotalPrice() {
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        return new Price(totalPrice);
    }

    public void mapInto(MenuEntity menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public MenuEntity getMenu() {
        return menu;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
