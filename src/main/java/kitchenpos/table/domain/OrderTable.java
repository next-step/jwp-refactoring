package kitchenpos.table.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {
    private static final String GROUPED_TABLE_CANNOT_CHANGE = "단체 지정된 테이블은 변경할 수 없습니다";
    private static final String NUMBER_OF_GUESTS_IS_NOT_NEGATIVE = "방문한 손님 수는 마이너스가 될 수 없습니다";
    private static final String EMPTY_TABLE_CANNOT_CHANGE = "빈 테이블은 변경할 수 없습니다";
    private static final String NOT_EMPTY_OR_GROUPED_TABLE_CANNOT_GROUP = "빈 테이블이 아니거나 이미 단체 지정되었다면 단체 지정 할 수 없습니다";
    private static final int MINIMUM_GUEST_NUMBER = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (tableGroupId != null) {
            throw new IllegalArgumentException(GROUPED_TABLE_CANNOT_CHANGE);
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MINIMUM_GUEST_NUMBER) {
            throw new IllegalArgumentException(NUMBER_OF_GUESTS_IS_NOT_NEGATIVE);
        }
        if (isEmpty()) {
            throw new IllegalArgumentException(EMPTY_TABLE_CANNOT_CHANGE);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void groupBy(Long tableGroupId) {
        if (!isEmpty() || isGrouped() || tableGroupId == null) {
            throw new IllegalArgumentException(NOT_EMPTY_OR_GROUPED_TABLE_CANNOT_GROUP);
        }
        this.tableGroupId = tableGroupId;
        empty = false;
    }

    private boolean isGrouped() {
        return tableGroupId != null;
    }

    public void ungroup() {
        tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
