package kitchenpos.ordertable.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {
    private static final String IS_GROUPED = "단체 테이블로 지정되어 있습니다.";
    private static final String NOT_USED = "사용중이지 않은 테이블은 손님 수를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {}

    public OrderTable(int numberOfGuests, boolean empty) {
        this.tableGroupId = null;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (isGrouped()) {
            throw new IllegalArgumentException(IS_GROUPED);
        }

        this.empty = empty;
    }

    public void groupBy(Long tableGroupId) {
        if (isGrouped()) {
            throw new IllegalArgumentException(IS_GROUPED);
        }

        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException(NOT_USED);
        }

        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public boolean isGrouped() {
        return tableGroupId != null;
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
}
