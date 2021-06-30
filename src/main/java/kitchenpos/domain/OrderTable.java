package kitchenpos.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> orders;

    @Column(name = "old_table_group_id")
    private Long tableGroupId;

    private NumberOfGuest numberOfGuests;

    private boolean empty;

    public OrderTable() {
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
        this.orders = orders;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuest(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, List<Order> orders, Long tableGroupId, NumberOfGuest numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.orders = orders;
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
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
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
        boolean isAllFinished = orders.stream()
                .allMatch(Order::isFinished);

        return isAllFinished;
    }

    public void changeNumberOfGuest(NumberOfGuest numberOfGuest) {
        if (empty) {
            throw new IllegalStateException();
        }

        this.numberOfGuests = numberOfGuest;
    }
}
