package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Embedded
    private MenuProductQuantity menuProductQuantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private Long productId;

    public MenuProduct() {
    }

    public MenuProduct(Menu menu, Long productId, long quantity) {
        this.menu = menu;
        this.productId = productId;
        this.menuProductQuantity = new MenuProductQuantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public MenuProductQuantity getMenuProductQuantity() {
        return menuProductQuantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return this.productId;
    }
}
