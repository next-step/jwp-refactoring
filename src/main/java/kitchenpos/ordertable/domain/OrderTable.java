package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import kitchenpos.constants.ErrorMessages;
import kitchenpos.ordertable.event.OderTableUngroupedEvent;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class OrderTable extends AbstractAggregateRoot<OrderTable> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Column(nullable = false)
    private boolean empty;

    public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        return new OrderTable(new NumberOfGuests(numberOfGuests), empty);
    }

    public OrderTable() {}

    private OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this.tableGroupId = null;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long tableGroupId, NumberOfGuests numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(tableGroupId, new NumberOfGuests(numberOfGuests), empty);
    }

    public OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void checkOrderTableGroupSetAble() {
        if (!empty) {
            throw new IllegalArgumentException(ErrorMessages.NOT_EMPTY_TABLE_CAN_SET_GROUP);
        }
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException(ErrorMessages.TABLE_ALREADY_SET_GROUP);
        }
    }

    public void groupOrderTable(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    @PrePersist
    public void ungroupOrderTable() {
        registerEvent(new OderTableUngroupedEvent(this));
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuestsValue() {
        return numberOfGuests.getNumberOfGuests();
    }

    public void updateNumberOfGuests(NumberOfGuests numberOfGuests) {
        checkNumberOfGuestsChangeAble();
        this.numberOfGuests = numberOfGuests;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        updateNumberOfGuests(new NumberOfGuests(numberOfGuests));
    }

    private void checkNumberOfGuestsChangeAble() {
        if (empty) {
            throw new IllegalArgumentException(ErrorMessages.CANNOT_CHANGE_NUMBER_OF_GUESTS_IF_ORDER_TABLE_EMPTY);
        }
    }

    public void checkOrderTableGrouped() {
        if (isGrouped()) {
            throw new IllegalArgumentException(ErrorMessages.GROUPED_ORDER_TABLE_CANNOT_CHANGE_EMPTY);
        }
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }

    public boolean isGrouped() {
        return tableGroupId != null;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId,
                that.tableGroupId) && Objects.equals(numberOfGuests, that.numberOfGuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
