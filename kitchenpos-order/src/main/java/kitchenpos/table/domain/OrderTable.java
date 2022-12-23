package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.exception.ErrorMessage;
import kitchenpos.common.vo.Empty;
import kitchenpos.common.vo.GuestCount;

@Entity
public class OrderTable {
    public static String ENTITY_NAME = "주문테이블";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_group_id")
    private Long tableGroupId;
    @Embedded
    private GuestCount guestCount;
    @Embedded
    private Empty empty;

    protected OrderTable() {}

    private OrderTable(int numberOfGuests, boolean empty) {
        this.guestCount = GuestCount.of(numberOfGuests);
        this.empty = Empty.of(empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public GuestCount getGuestCounts() {
        return guestCount;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void group(Long tableGroupId) {
        validateGroupedWhenGroup();
        validateEmptyWhenGroup();
        this.tableGroupId = tableGroupId;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public void updateEmptyStatus(Empty empty) {
        validateGroupedWhenChangeEmpty();
        this.empty = empty;
    }

    public void updateNumberOfGuest(GuestCount guestCounts) {
        validateEmptyWhenChangeNumberOfGuests();
        this.guestCount = guestCounts;
    }

    private void validateGroupedWhenGroup() {
        if (isGrouped()) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_ALREADY_GROUPED);
        }
    }

    private void validateGroupedWhenChangeEmpty() {
        if (isGrouped()) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_CHANGE_EMPTINESS_WHEN_TABLE_GROUPED);
        }
    }

    private void validateEmptyWhenChangeNumberOfGuests() {
        if (isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_CHANGE_NUMBER_OF_GUESTS_WHEN_TABLE_IS_EMPTY);
        }
    }

    private void validateEmptyWhenGroup() {
        if (!isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_IS_NOT_ALL_EMPTY);
        }
    }

    public boolean isEmpty() {
        return this.empty.isEmpty();
    }

    public boolean isGrouped() {return Objects.nonNull(this.getTableGroupId());}
}
