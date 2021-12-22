package kitchenpos.domain;

import kitchenpos.exception.NotSupportUngroupException;
import kitchenpos.exception.TableEmptyUpdateException;
import kitchenpos.exception.TableGuestNumberUpdateException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @OneToMany(mappedBy = "orderTable", cascade = {CascadeType.ALL})
    private final List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    public OrderTable(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public void changeEmpty(boolean changeEmpty) {
        if (checkTableGroup() || checkOrders()) {
            throw new TableEmptyUpdateException();
        }
        this.empty = changeEmpty;
    }

    private boolean checkOrders() {
        return !orders.isEmpty() && !orderCompleted();
    }

    private boolean checkTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    private boolean orderCompleted() {
        return orders.stream().allMatch(Order::isCompleted);
    }

    public void updateNumberOfGuests(Integer newNumberOfGuests) {
        if (empty) {
            throw new TableGuestNumberUpdateException();
        }

        this.numberOfGuests = NumberOfGuests.of(newNumberOfGuests);
    }

    public OrderTable addOrder(Order order) {
        this.orders.add(order);
        return this;
    }

    public OrderTable groupBy(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
        return this;
    }

    public void ungroup() {
        if (checkOrders()) {
            throw new NotSupportUngroupException();
        }
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
