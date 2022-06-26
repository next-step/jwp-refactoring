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
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @JoinColumn(name = "product_id")
    private Long productId;

    private long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public void validateHasProduct() {
        if (Objects.isNull(productId)) {
            throw new CannotCreateException(ExceptionType.CONTAINS_NOT_EXIST_PRODUCT);
        }
    }

    public Price getTotalPrice() {
//        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
//        return new Price(totalPrice);
        return new Price(BigDecimal.valueOf(500L));
    }

    public void mapInto(Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
