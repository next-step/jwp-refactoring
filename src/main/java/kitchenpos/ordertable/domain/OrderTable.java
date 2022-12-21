package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.error.ErrorEnum;
@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;
    private Long tableGroupId;

    protected OrderTable() {}

    public OrderTable(Long id, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateNumberOfGuest(NumberOfGuests numberOfGuests) {
        validateShouldNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateShouldNotEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException(ErrorEnum.ORDER_TABLE_IS_EMPTY.message());
        }
    }

    private void validateHasTableGroup() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException(ErrorEnum.ALREADY_GROUP.message());
        }
    }
    public void updateTableGroup(Long tableGroupId) {
        if (!isEmpty()) {
            throw new IllegalArgumentException(ErrorEnum.ORDER_TABLE_IS_NOT_EMPTY.message());
        }
        updateEmpty(false);
        this.tableGroupId = tableGroupId;
    }

    public void updateEmpty(boolean empty) {
        validateHasTableGroup();
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroupId = null;
        updateEmpty(true);
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
