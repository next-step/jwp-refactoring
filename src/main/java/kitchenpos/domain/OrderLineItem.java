package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long menuId;


    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {

    }

    private OrderLineItem(Long menuId, long quantity) {
        validMenu(menuId);
        this.quantity = Quantity.of(quantity);
        this.menuId = menuId;

    }

    public static OrderLineItem of(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    private void validMenu(Long menuId) {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException("메뉴가 없습니다.");
        }
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity.value();
    }

    public void changeSeq(long seq) {
        this.seq = seq;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getId() {
        return id;
    }

    public void changeOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
