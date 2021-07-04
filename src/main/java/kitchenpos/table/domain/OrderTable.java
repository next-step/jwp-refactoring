package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import kitchenpos.order.domain.Order;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    @OneToMany(mappedBy = "orderTableId")
    private List<Order> orders = new ArrayList<>();

    public OrderTable() {}

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty, List<Order> orders) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(id, null, numberOfGuests, empty);
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

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public void chargedBy(TableGroup tableGroup) {
        this.empty = true;
        this.tableGroupId = tableGroup.getId();
    }

    public boolean isCooking() {
        return false;
    }

    public boolean isImmutableOrder() {
        return orders.stream().anyMatch(Order::isImmutableOrder);
    }

    public boolean isAvaliableTable() {
        return !isEmpty() || hasTableGroup();
    }

    public void ungroup() {
        tableGroupId = null;
    }

    public void updateEmpty(OrderTableRequest orderTableRequest) {
        validationEmpty();
        this.empty = orderTableRequest.isEmpty();
    }

    private void validationEmpty() {
        if (hasTableGroup()) {
            throw new IllegalArgumentException();
        }

        if (isImmutableOrder()) {
            throw new IllegalArgumentException();
        }

    }

    public void updateGuests(OrderTableRequest orderTableRequest) {
        validationGuests(orderTableRequest);
        this.numberOfGuests = orderTableRequest.getNumberOfGuests();
    }

    private void validationGuests(OrderTableRequest orderTableRequest) {
        if (orderTableRequest.getNumberOfGuests() < 0) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
