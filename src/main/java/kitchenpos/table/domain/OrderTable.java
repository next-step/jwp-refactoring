package kitchenpos.table.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {
    private static final String ERR_TEXT_ALREADY_CONTAINS_TABLE_GROUP = "테이블 그룹에 속해있어 빈 테이블로 설정할 수 없습니다.";
    private static final String ERR_TEXT_CAN_NOT_CHANGE_GUEST_WHEN_TABLE_IS_EMPTY =
        "요청한 주문 테이블이 존재하지 않는(빈 테이블) 경우 게스트 수를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    protected OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

    public void throwIllegalExceptionWhenOrderTableIsEmpty() {
        if (this.empty) {
            throw new IllegalArgumentException(ERR_TEXT_CAN_NOT_CHANGE_GUEST_WHEN_TABLE_IS_EMPTY);
        }
    }

    public void throwIllegalExceptionWhenTableGroupIsNull() {
        if (this.tableGroupId != null) {
            throw new IllegalArgumentException(ERR_TEXT_ALREADY_CONTAINS_TABLE_GROUP);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }


    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
