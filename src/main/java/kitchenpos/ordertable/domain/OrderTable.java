package kitchenpos.ordertable.domain;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.Order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    private int numberOfGuests;

    private boolean empty;


    public OrderTable() {
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    public OrderTable(Long id, int numberOfGuests) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests) {
        this(id, tableGroup, numberOfGuests, false);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void validateAddableOrderTable() {
        if (!isEmpty() || Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateCompleted() {
        for (Order order: orders) {
            order.validateCompleted();
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty() {
        validateChangeableOrderTable();
        validateNotProcessing();
        empty = true;
        orders = Collections.emptyList();
    }

    public void validateNotProcessing() {
        for (Order order: orders) {
            order.validateNotProcessing();
        }
    }

    private void validateChangeableOrderTable() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        order.setOrderTable(this);
    }
}
