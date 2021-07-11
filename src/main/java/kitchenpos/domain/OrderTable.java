package kitchenpos.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kitchenpos.domain.OrderStatus.*;

@Entity
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "table_group_id")
    @ManyToOne
    private TableGroup tableGroup;

    private NumberOfGuests numberOfGuests;

    private boolean empty;

    @OneToMany(mappedBy = "orderTable", orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void addOrder(Order order) {
        if (!orders.contains(order)) {
            orders.add(order);
        }
    }

    public void changeEmpty(boolean empty) {
        verifyChangeableEmpty();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        verifyChangeable();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        if (Objects.nonNull(tableGroup)) {
            tableGroup.addOrderTable(this);
        }
    }

    private void verifyChangeable() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void verifyChangeableEmpty() {
        if (orders.stream()
                .anyMatch(order -> (order.getOrderStatus() == COOKING || order.getOrderStatus() == MEAL))) {
            throw new IllegalArgumentException("주문테이블의 주문상태가 조리나 식사입니다.");
        }
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체지정이 되어있으면 안됩니다.");
        }
    }

    public boolean isCompletionAllOrders() {
        return orders.stream()
                .allMatch(order -> order.getOrderStatus() == COMPLETION);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
