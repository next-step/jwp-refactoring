package kitchenpos.order.domain;

import kitchenpos.global.BaseTimeEntity;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "menu_id", length = 20, nullable = false)
    private Long menu;

    @Column(name = "quantity", length = 20, nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menu, final long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public void addOrder(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

}
