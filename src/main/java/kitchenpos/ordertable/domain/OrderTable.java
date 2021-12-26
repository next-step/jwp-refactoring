package kitchenpos.ordertable.domain;

import kitchenpos.common.domain.Empty;
import kitchenpos.common.domain.NumberOfGuests;
import kitchenpos.common.exception.NotEmptyOrderTableStatusException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.Order;

import javax.persistence.Embedded;
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

    @Embedded
    private NumberOfGuests numberOfGuests;


    @Embedded
    private Empty empty;


    public OrderTable() {
    }

    public OrderTable(int numberOfGuests) {
        this(null, numberOfGuests);
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests) {
        this(null, tableGroup, numberOfGuests);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        checkEmpty(numberOfGuests);
    }

    private void checkEmpty(int numberOfGuests) {
        if (numberOfGuests == 0) {
            empty = new Empty(true);
            return;
        }

        empty = new Empty(false);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
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

    public void validateAddableOrderTable() {
        if (!isEmpty() || Objects.nonNull(tableGroup)) {
            throw new NotEmptyOrderTableStatusException();
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests.changeNumberOfGuests(numberOfGuests);
        checkEmpty(numberOfGuests);
    }

    public void changeEmpty() {
        validateChangeableEmpty();
        empty = new Empty(true);
        orders = Collections.emptyList();
    }

    private void validateChangeableEmpty() {
        validateNotHavingTableGroup();
        validateNotProcessing();
    }

    private void validateNotHavingTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new NotEmptyOrderTableStatusException();
        }
    }

    public void validateNotProcessing() {
        for (Order order: orders) {
            order.validateNotProcessingWhenChangeEmpty();
        }
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        order.decideOrderTable(this);
    }

    public void ungroupTableGroup() {
        for (Order order: orders) {
            order.validateCompletedWhenUngroup();
        }

        this.tableGroup = null;
    }
}
