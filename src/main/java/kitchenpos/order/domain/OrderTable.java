package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.domain.Empty;
import kitchenpos.common.exception.BadRequestException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    @Embedded
    private Orders orders;

    protected OrderTable() {
    }

    private OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new Empty(empty);
        this.orders = new Orders();
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public static OrderTable of(long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public Empty isEmpty() {
        return empty;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void changeTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty.changeEmpty(false);
    }

    public void deleteTableGroup() {
        tableGroupId = null;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateChangeableNumberOfGuests();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void changeEmpty(final boolean empty) {
        validateChangeableEmpty();
        validateNotCompletionOrderStatus();
        this.empty.changeEmpty(empty);
    }

    public boolean isPossibleIntoTableGroup() {
        return empty.getValue() && Objects.isNull(tableGroupId);
    }

    private void validateChangeableNumberOfGuests() {
        if (empty.getValue()) {
            throw new BadRequestException(CANNOT_CHANGE_STATUS);
        }
    }

    private void validateChangeableEmpty() {
        if (Objects.nonNull(tableGroupId)) {
            throw new BadRequestException(CANNOT_CHANGE_STATUS);
        }
    }

    public void validateNotCompletionOrderStatus() {
        orders.validateNotCompletionOrderStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderTable that = (OrderTable)o;
        return empty == that.empty && Objects.equals(id, that.id) && Objects.equals(numberOfGuests,
            that.numberOfGuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfGuests, empty);
    }
}
