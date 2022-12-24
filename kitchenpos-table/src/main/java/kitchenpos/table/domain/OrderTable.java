package kitchenpos.table.domain;

import static javax.persistence.GenerationType.*;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.exception.CannotChangeByOrderStatusException;
import kitchenpos.table.exception.ChangeEmptyGroupException;
import kitchenpos.table.exception.EmptyTableException;
import kitchenpos.table.exception.GroupTableException;
import kitchenpos.table.exception.InvalidNumberOfGuestsException;

@Entity
public class OrderTable {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private Orders orders = new Orders();

    public OrderTable() {
    }

    public OrderTable(Boolean empty) {
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Order order) {
        this.orders.add(order);
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void group(TableGroup tableGroup) {
        if (!this.empty || Objects.nonNull(this.tableGroup)) {
            throw new GroupTableException();
        }
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        if (orders.hasCannotChangeOrder()) {
            throw new CannotChangeByOrderStatusException();
        }
        this.tableGroup = null;
    }

    public void changeEmpty(Boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new ChangeEmptyGroupException();
        }
        if (orders.hasCannotChangeOrder()) {
            throw new CannotChangeByOrderStatusException();
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        if (numberOfGuests <= 0) {
            throw new InvalidNumberOfGuestsException();
        }
        if (this.empty) {
            throw new EmptyTableException();
        }
        this.numberOfGuests = numberOfGuests;
    }

}
