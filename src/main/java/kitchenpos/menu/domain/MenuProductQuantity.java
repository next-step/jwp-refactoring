package kitchenpos.menu.domain;

import javax.persistence.Embeddable;

@Embeddable
public class MenuProductQuantity {

    private long quantity;

    protected MenuProductQuantity() {
    }

    public MenuProductQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("메뉴 상품의 수량은 최소 0개 이상만 설정할 수 있습니다[quantity:" + quantity + "]");
        }
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}
