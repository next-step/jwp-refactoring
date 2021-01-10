package kitchenpos.domain.orderTable;

import kitchenpos.domain.orderTable.exceptions.InvalidTryChangeEmptyException;
import kitchenpos.domain.orderTable.exceptions.InvalidTryChangeGuestsException;
import kitchenpos.domain.orderTable.exceptions.InvalidTryGroupingException;

import javax.persistence.*;

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

    OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        validateCreation(numberOfGuests, empty);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public boolean isGrouped() {
        return tableGroupId != null;
    }

    public void changeEmpty(final boolean empty) {
        if (this.isGrouped()) {
            throw new InvalidTryChangeEmptyException("단체 지정된 주문 테이블의 비움 상태를 바꿀 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (this.isEmpty()) {
            throw new InvalidTryChangeGuestsException("비어있는 주문 테이블의 방문한 손님 수를 바꿀 수 없습니다.");
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void group(final Long tableGroupId) {
        validateGrouping();
        changeEmpty(false);
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }

    private void validateCreation(final int numberOfGuest, final boolean empty) {
        if (empty && numberOfGuest != 0) {
            throw new InvalidOrderTableException("비어 있는 경우 손님수는 0명이어야 한다.");
        }
    }

    private void validateGrouping() {
        if(this.isGrouped()) {
            throw new InvalidTryGroupingException("이미 단체 지정된 주문 테이블을 단체 지정할 수 없습니다.");
        }
        if(!this.isEmpty()) {
            throw new InvalidTryGroupingException("비어 있지 않은 주문 테이블을 단체 지정할 수 없습니다.");
        }
    }
}
