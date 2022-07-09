package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.global.domain.Quantity;
import org.springframework.util.ObjectUtils;

@Table(name = "menu_product")
@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {

    }

    public MenuProduct(Long productId, int quantity) {
        validProduct(productId);
        this.productId = productId;
        this.quantity = Quantity.from(quantity);
    }

    public MenuProduct(Long seq, Menu menu, Long productId, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long productId, int quantity) {
        return new MenuProduct(productId, quantity);
    }

    private void validProduct(Long productId) {
        if (ObjectUtils.isEmpty(productId)) {
            throw new IllegalArgumentException("메뉴에 등록할 상품이 존재하지 않습니다.");
        }
    }

    public void changeMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Menu getMenu() {
        return menu;
    }
}
