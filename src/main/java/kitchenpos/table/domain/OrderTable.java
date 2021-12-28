package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.exception.NegativeNumberOfGuestsException;
import kitchenpos.table.application.OrderValidator;
import kitchenpos.table.exception.CannotChangeEmptyException;
import kitchenpos.table.exception.CannotChangeNumberOfGuestsException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {
    public static final int ZERO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void changeTableGroup(Long tableGroupId) {
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateChangeNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateChangeNumberOfGuests(int numberOfGuests) {
        if (this.empty) {
            throw new CannotChangeNumberOfGuestsException();
        }
        if (numberOfGuests < ZERO) {
            throw new NegativeNumberOfGuestsException();
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(OrderValidator orderValidator, final boolean empty) {
        validateChangeEmpty(orderValidator);
        this.empty = empty;
    }

    private void validateChangeEmpty(OrderValidator orderValidator) {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new CannotChangeEmptyException();
        }
        orderValidator.canUngroupOrChange(this.id);
    }

    public void ungroup() {
        this.tableGroupId = null;
    }
}
