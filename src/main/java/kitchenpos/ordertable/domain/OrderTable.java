package kitchenpos.ordertable.domain;

import kitchenpos.common.domain.Empty;
import kitchenpos.common.domain.NumberOfGuests;
import kitchenpos.order.domain.Order;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OrderTable {
    public static final int EMPTY_TABLE_NUMBER = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;


    public OrderTable() {
    }

    public OrderTable(int numberOfGuests) {
        this(null, numberOfGuests);
    }

    public OrderTable(Long tableGroupId, int numberOfGuests) {
        this(null, tableGroupId, numberOfGuests);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        checkEmpty(numberOfGuests);
    }

    private void checkEmpty(int numberOfGuests) {
        if (numberOfGuests == EMPTY_TABLE_NUMBER) {
            empty = new Empty(true);
            orders = new ArrayList<>();
            return;
        }

        empty = new Empty(false);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests.changeNumberOfGuests(numberOfGuests);
        checkEmpty(numberOfGuests);
    }

    public void changeEmpty() {
        this.numberOfGuests.changeNumberOfGuests(EMPTY_TABLE_NUMBER);
        checkEmpty(EMPTY_TABLE_NUMBER);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        order.decideOrderTable(this);
    }

    public void ungroupTableGroup() {
        this.tableGroupId = null;
    }
}
