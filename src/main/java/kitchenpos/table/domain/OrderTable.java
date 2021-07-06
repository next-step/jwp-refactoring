package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.order.domain.Order;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.exception.AlreadyExsistGroupException;
import kitchenpos.table.exception.EmptyTableException;
import kitchenpos.table.exception.NoGuestsException;
import kitchenpos.table.exception.OrderUsingException;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private Boolean empty;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected OrderTable() {}

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, Boolean empty, List<Order> orders) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, Boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, int numberOfGuests, Boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }

    public Boolean hasTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    public void chargedBy(TableGroup tableGroup) {
        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public Boolean isImmutableOrder() {
        return orders.stream().anyMatch(Order::isImmutableOrder);
    }

    public Boolean isAvaliableTable() {
        return !isEmpty() || hasTableGroup();
    }

    public void ungroup() {
        tableGroup = null;
        this.empty = true;
    }

    public void updateEmpty(OrderTableRequest orderTableRequest) {
        validationEmpty();
        this.empty = orderTableRequest.isEmpty();
    }

    private void validationEmpty() {
        if (hasTableGroup()) {
            throw new AlreadyExsistGroupException();
        }

        if (isImmutableOrder()) {
            throw new OrderUsingException();
        }

    }

    public void updateGuests(OrderTableRequest orderTableRequest) {
        validationGuests(orderTableRequest);
        this.numberOfGuests = orderTableRequest.getNumberOfGuests();
    }

    private void validationGuests(OrderTableRequest orderTableRequest) {
        if (orderTableRequest.getNumberOfGuests() < 0) {
            throw new NoGuestsException();
        }

        if (isEmpty()) {
            throw new EmptyTableException();
        }
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }
}
