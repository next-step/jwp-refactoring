package kitchenpos.table.domain;

import static java.util.Objects.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    private TableGroupId tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {}

    OrderTable(Long id, TableGroupId tableGroupId, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(NumberOfGuests.valueOf(numberOfGuests), empty);
    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public TableGroupId getTableGroupId() {
        return tableGroupId;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 방문 손님 수를 수정할 수 없습니다.");
        }
        this.numberOfGuests = NumberOfGuests.valueOf(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean isEmpty, TableChangeEmptyValidator externalValidator) {
        externalValidator.validate(getId());
        validateNotGrouped();
        this.empty = isEmpty;
    }

    public boolean isGrouped() {
        return nonNull(tableGroupId);
    }

    public void grouped(TableGroupId tableGroupId) {
        this.tableGroupId = tableGroupId;
        emptyOff();
    }

    public void ungrouped(TableUngroupValidator ungroupValidator) {
        ungroupValidator.validate(getId());
        this.tableGroupId = null;
    }

    private void emptyOff() {
        this.empty = false;
    }

    private void validateNotGrouped() {
        if (isGrouped()) {
            throw new IllegalArgumentException("그룹 설정이 되어 있는 테이블은 주문 등록 불가 상태로 바꿀 수 없습니다.");
        }
    }

}
