package kitchenpos.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.exception.AlreadyExsistGroupException;
import kitchenpos.exception.EmptyTableException;
import kitchenpos.exception.NoGuestsException;
import kitchenpos.exception.OrderUsingException;
import kitchenpos.order.Order;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;

    private Boolean empty;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private List<Order> orders = new ArrayList<>();

    protected OrderTable() {}

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, Boolean empty, List<Order> orders) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
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
        return Objects.nonNull(tableGroupId);
    }

    public void chargedBy(TableGroup tableGroup) {
        this.empty = false;
        this.tableGroupId = tableGroup.getId();
    }

    public Boolean isImmutableOrder() {
        return orders.stream().anyMatch(Order::isImmutableOrder);
    }

    public Boolean isAvaliableTable() {
        return !isEmpty() || hasTableGroup();
    }

    public void ungroup() {
        tableGroupId = null;
        this.empty = true;
    }

    public void updateEmpty(Boolean isEmpty) {
        validationEmpty();
        this.empty = isEmpty;
    }

    private void validationEmpty() {
        if (hasTableGroup()) {
            throw new AlreadyExsistGroupException();
        }

        if (isImmutableOrder()) {
            throw new OrderUsingException();
        }

    }

    public void updateGuests(int numberOfGuests) {
        validationGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validationGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NoGuestsException();
        }

        if (isEmpty()) {
            throw new EmptyTableException();
        }
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroupId)) {
            return null;
        }
        return tableGroupId;
    }
}
