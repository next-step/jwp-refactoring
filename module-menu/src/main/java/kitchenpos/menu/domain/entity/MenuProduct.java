package kitchenpos.menu.domain.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.menu.domain.value.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Long productId;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = Quantity.of(quantity);
    }

    public MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = Quantity.of(quantity);
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

    public Quantity getQuantity() {
        return quantity;
    }

    public void toMenu(Menu menu) {
        this.menu = menu;
    }
}
