package kitchenpos.ordertable.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.ordertable.exception.CanNotEditOrderTableEmptyByGroupException;
import kitchenpos.ordertable.exception.CanNotEditOrderTableNumberOfGuestsByEmptyException;
import kitchenpos.tablegroup.exception.CanNotGroupByEmptyException;
import kitchenpos.tablegroup.exception.CanNotGroupByGroupingAlreadyException;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return of(null, null, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public void group(Long tableGroupId) {
        validateGroup();
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    private void validateGroup() {
        if (isInGroup()) {
            throw new CanNotGroupByGroupingAlreadyException();
        }
        if (!empty) {
            throw new CanNotGroupByEmptyException();
        }
    }

    private boolean isInGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void changeEmptyIfNotTableGroup(boolean empty) {
        if (isInGroup()) {
            throw new CanNotEditOrderTableEmptyByGroupException();
        }
        this.empty = empty;
    }

    public void changeNumberOfGuestsIfNotEmpty(int numberOfGuests) {
        if (empty) {
            throw new CanNotEditOrderTableNumberOfGuestsByEmptyException();
        }
        this.numberOfGuests.changeNumberOfGuests(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }
}
