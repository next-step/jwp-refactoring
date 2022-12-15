package kitchenpos.domain;

import static javax.persistence.GenerationType.*;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.dto.OrderTableRequest;
import kitchenpos.exception.CannotChangeByOrderStatusException;
import kitchenpos.exception.ChangeEmptyGroupException;
import kitchenpos.exception.EmptyTableException;
import kitchenpos.exception.GroupTableException;
import kitchenpos.exception.InvalidNumberOfGuestsException;

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

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(OrderTableRequest request) {
        OrderTable table = new OrderTable();
        table.numberOfGuests = request.getNumberOfGuests();
        table.empty = request.isEmpty();
        return table;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
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

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}
