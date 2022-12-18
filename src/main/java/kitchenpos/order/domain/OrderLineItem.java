package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {
    public static final String MENU_NULL_EXCEPTION_MESSAGE = "메뉴가 없을 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Orders order;
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Orders order, Long menuId, int quantity) {
        validate(menuId);
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(long seq, Orders order, long menuId, long quantity) {
        this.id = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    private void validate(Long menuId) {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException(MENU_NULL_EXCEPTION_MESSAGE);
        }
    }

    public Long getSeq() {
        return id;
    }

    public void setSeq(final Long seq) {
        this.id = seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }
}
