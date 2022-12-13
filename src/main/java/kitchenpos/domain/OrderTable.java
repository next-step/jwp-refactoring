package kitchenpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @JsonIgnore
    @OneToMany(mappedBy = "orderTable")
    private final List<Order> orders = new ArrayList<>();
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    private OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
        
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }

        if (isNotCompletedOrders()) {
            throw new IllegalArgumentException();
        }

        this.empty = empty;
    }

    private boolean isNotCompletedOrders() {
        return orders.stream().anyMatch(order -> isNotCompleted(order.getOrderStatus()));
    }

    private boolean isNotCompleted(final String orderStatus) {
        return orderStatus.equals(OrderStatus.COOKING.name()) || orderStatus.equals(OrderStatus.MEAL.name());
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void createOrder(final Order order) {
        this.orders.add(order);
    }
}
