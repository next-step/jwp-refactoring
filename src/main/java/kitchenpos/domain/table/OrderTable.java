package kitchenpos.domain.table;

import kitchenpos.domain.order.Orders;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_group_id")
    private OrderTableGroup orderTableGroup;

    @OneToMany(mappedBy = "orderTable", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Orders> orders = new ArrayList<>();

    @Embedded
    private GuestNumber guestNumber;

    private boolean empty;

    protected OrderTable() {}

    public OrderTable(OrderTableGroup orderTableGroup, GuestNumber guestNumber, boolean empty) {
        this.orderTableGroup = orderTableGroup;
        this.guestNumber = guestNumber;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public GuestNumber getGuestNumber() {
        return guestNumber;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void addOrder(Orders order) {
        orders.add(order);
    }

    public void changeOrderTableGroup(OrderTableGroup orderTableGroup) {
        this.orderTableGroup = orderTableGroup;
    }

    public void checkOrderStatus() {
        if (!orders.stream().allMatch(Orders::isCompletion)) {
            throw new IllegalArgumentException("식사완료 상태가 아닌 주문이 존재합니다");
        }
    }

    public void checkOrderTableGroup() {
        if (orderTableGroup != null) {
            throw new IllegalArgumentException("그룹화 상태 입니다");
        }
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeOrderTableStatus(boolean empty) {
        checkOrderStatus();
        checkOrderTableGroup();
        changeEmpty(empty);
    }

    private void checkOrder() {
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("주문을 받지 않은 테이블 입니다");
        }
    }

    public void checkEmpty() {
        if (empty) {
            throw new IllegalArgumentException("이용중인 테이블이 아닙니다");
        }
    }

    public void changeGuestNumber(GuestNumber guestNumber) {
        checkOrder();
        checkEmpty();
        this.guestNumber = guestNumber;
    }
}
