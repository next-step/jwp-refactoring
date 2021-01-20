package kitchenpos.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Integer numberOfGuests;
    @Column
    private Boolean empty;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToMany(mappedBy = "orderTable")
    private final List<Order> orders = new ArrayList<>();

    public OrderTable(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTable() {
    }

    public boolean hasTableGroup() {
        return tableGroup != null;
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

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmpty() {
        return !empty;
    }

    public void occupy(TableGroup tableGroup) {
        this.updateGroup(tableGroup);
        this.empty = false;
    }

    public void updateGroup(TableGroup tableGroup) {
        tableGroup.getOrderTables().add(this);
        this.tableGroup = tableGroup;
    }

    public void releaseInGroup() {
        this.tableGroup = null;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isNotPaymentFinished() {
        return orders.stream()
                .anyMatch(Order::isNotCompleted);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
