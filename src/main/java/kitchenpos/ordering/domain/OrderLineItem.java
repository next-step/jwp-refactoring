package kitchenpos.ordering.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id")
//    private Ordering order;

    @Column
    private Long orderId;

    @Column
    private Long menuId;

    @Column
    private long quantity;

    public OrderLineItem() { }

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long orderId, Long menuId, long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long id, Long orderId, Long menuId, long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

//    public OrderLineItem(Ordering order, Long menuId, long quantity) {
//        this.orderId = order.getId();
//        this.menuId = menuId;
//        this.quantity = quantity;
//    }
//
//    public OrderLineItem(Long id, Ordering order, Long menuId, long quantity) {
//        this.id = id;
//        this.orderId = order.getId();
//        this.menuId = menuId;
//        this.quantity = quantity;
//    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity && Objects.equals(id, that.id) && Objects.equals(orderId, that.orderId) && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, menuId, quantity);
    }

    //    public void isIn(Ordering order) {
//        this.order = order;
//    }

    public void isIn(Ordering order) {
        this.orderId = order.getId();
    }

}
