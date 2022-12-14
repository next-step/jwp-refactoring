package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {
    private static final int ZERO = 0;
    private static final String REQUIRED_MENU = "메뉴는 필수 값 입니다.";
    private static final String INVALID_QUANTITY = "수량은 1 이상 이어야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long menuId;

    private long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Long menuId, long quantity) {
        validate(menuId, quantity);
        this.menuId = menuId;
        this.quantity = quantity;
    }

    private void validate(Long menuId, long quantity) {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException(REQUIRED_MENU);
        }
        if (quantity <= ZERO) {
            throw new IllegalArgumentException(INVALID_QUANTITY);
        }
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
