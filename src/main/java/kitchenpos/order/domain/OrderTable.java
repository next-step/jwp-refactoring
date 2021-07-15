package kitchenpos.order.domain;

import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_table_id")
    private List<Order> orders;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = new ArrayList<>();
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }


    public void validateOrderStatusNotInCookingAndMeal() {
        List<OrderStatus> orderStatuses = getOrderStatuses();

        if (orderStatuses.contains(OrderStatus.COOKING) || orderStatuses.contains(OrderStatus.MEAL)) {
            throw new OrderTableException("올바르지 않은 주문상태가 포함되어있습니다", orderStatuses);
        }
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void validateTableGroupIsNull() {
        if (this.tableGroup != null) {
            throw new OrderTableException("테이블 그룹은 비어있어야 합니다");
        }
    }

    public Long getId() {
        return id;
    }

    public OrderTable(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void addTableGroup(TableGroup tableGroup) {
        updateEmpty(false);
        this.tableGroup = tableGroup;
    }

    public void updateEmpty(boolean empty) {
        this.empty = empty;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public List<Order> getOrders() {
        return orders;
    }

    private List<OrderStatus> getOrderStatuses() {
        return orders.stream()
            .map(Order::getOrderStatus)
            .collect(Collectors.toList());
    }
}
