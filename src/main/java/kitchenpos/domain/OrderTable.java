package kitchenpos.domain;

import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private Orders orders = new Orders();

    @Column(name = "old_table_group_id")
    private Long tableGroupId;

    private NumberOfGuest numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, Orders orders, NumberOfGuest numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.orders = orders;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(NumberOfGuest numberOfGuest, boolean empty) {
        this.numberOfGuests = numberOfGuest;
        this.empty = empty;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuest(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuest(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, List<Order> orders, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.orders = new Orders(orders);
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuest(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, List<Order> orders, Long tableGroupId, NumberOfGuest numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.orders = new Orders(orders);
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public List<Order> getOrders() {
        return orders.toCollection();
    }

    public NumberOfGuest getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(NumberOfGuest numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void ungroup() {
        if (!isUnGroupable()) {
            throw new IllegalStateException();
        }
    }

    public boolean isUnGroupable() {
        return orders.isAllFinished();
    }

    public void changeNumberOfGuest(NumberOfGuest numberOfGuest) {
        if (empty) {
            throw new IllegalStateException();
        }

        this.numberOfGuests = numberOfGuest;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (!orders.isAllFinished()) {
            throw new IllegalStateException();
        }

        this.empty = empty;
    }
}
