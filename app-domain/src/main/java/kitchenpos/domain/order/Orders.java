package kitchenpos.domain.order;

import kitchenpos.domain.BaseDateTime;
import kitchenpos.domain.table.OrderTable;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Orders extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineMenu> orderLineMenus = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    protected Orders() {}

    public Orders(OrderTable orderTable, OrderStatus orderStatus) {
        orderTable.checkEmpty();
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public List<OrderLineMenu> getOrderLineMenus() {
        return orderLineMenus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void addOrderLineMenu(OrderLineMenu orderLineMenu) {
        orderLineMenus.add(orderLineMenu);
    }

    public void changeOrderStatus(String orderStatus) {
        if (isCompletion()) {
            throw new IllegalArgumentException("식사 완료 상태입니다");
        }
        this.orderStatus = OrderStatus.getOrderStatus(orderStatus);
    }

    public boolean isCompletion() {
        return orderStatus == OrderStatus.COMPLETION;
    }
}
