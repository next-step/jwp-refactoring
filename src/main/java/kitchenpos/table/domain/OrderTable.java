package kitchenpos.table.domain;

import static kitchenpos.exception.KitchenposExceptionMessage.ALREADY_INCLUDE_TABLE_GROUP;
import static kitchenpos.exception.KitchenposExceptionMessage.EMPTY_GUESTS;
import static kitchenpos.exception.KitchenposExceptionMessage.GUESTS_CANNOT_LOWER_THAN_MIN;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.exception.KitchenposException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
        // empty
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void checkEmptyAndNotIncludeOrderTable() {
        checkEmpty();
        if (Objects.nonNull(this.tableGroupId)) {
            throw new KitchenposException(ALREADY_INCLUDE_TABLE_GROUP);
        }
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests <= 0) {
            throw new KitchenposException(GUESTS_CANNOT_LOWER_THAN_MIN);
        }
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    private void checkEmpty() {
        if (this.empty) {
            throw new KitchenposException(EMPTY_GUESTS);
        }
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }
}
