package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long tableGroupId, int numberOfGuests) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
    public void referenceTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void updateNumberOfGuests(final NumberOfGuests numberOfGuests) {
        validateNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    public void validateNotEmpty() {
        if (empty) {
            throw new KitchenposException(KitchenposErrorCode.TABLE_IS_EMPTY);
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }

    public int getGuestNumber() {
        return numberOfGuests.getNumberOfGuests();
    }

    public void checkNotGrouped() {
        if (Objects.nonNull(tableGroupId)) {
            throw new KitchenposException(KitchenposErrorCode.TABLE_IS_IN_GROUP);
        }
    }

    public boolean cannotBeGrouped() {
        return !empty || Objects.nonNull(tableGroupId);
    }
}
