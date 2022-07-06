package kitchenpos.domain.table;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    public static final String CHANGE_EMPTY_TARGET_ORDER_TABLE_IS_GROUPED_ERROR_MESSAGE = "테이블 그룹에 할당된 주문 테이블의 좌석 상태는 수정할 수 없습니다.";
    public static final int MINIMUM_NUMBER_OF_GUESTS_COUNT = 0;
    public static final String LEEN_THAN_MINIMUM_NUMBER_OF_GUEST_COUNT_ERROR_MESSAGE = String
        .format("유효하지 못한 인원 수입니다. %d명 이상 입력해주세요.", MINIMUM_NUMBER_OF_GUESTS_COUNT);
    public static final String EMPTY_ORDER_TABLE_CHANGE_NUMBER_OF_GUESTS_ERROR_MESSAGE = "비어 있는 주문테이블의 인원 수는 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "table_group_id",
        foreignKey = @ForeignKey(name = "FK_ORDER_TABLE_TO_TABLE_GORUP")
    )
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {

    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.tableGroup = null;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        validateTableGroupAllocatedOrderTable();
        this.empty = empty;
    }

    private void validateTableGroupAllocatedOrderTable() {
        if (isGroupTable()) {
            throw new IllegalArgumentException(CHANGE_EMPTY_TARGET_ORDER_TABLE_IS_GROUPED_ERROR_MESSAGE);
        }
    }

    public boolean isGroupTable() {
        return tableGroup != null;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void allocateTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void deallocateTableGroup() {
        this.tableGroup = null;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void validateNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuestsOverZero();
        validateEmptyOrderTable();
    }

    private void validateNumberOfGuestsOverZero() {
        if (numberOfGuests < MINIMUM_NUMBER_OF_GUESTS_COUNT) {
            throw new IllegalArgumentException(LEEN_THAN_MINIMUM_NUMBER_OF_GUEST_COUNT_ERROR_MESSAGE);
        }
    }

    private void validateEmptyOrderTable() {
        if (isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ORDER_TABLE_CHANGE_NUMBER_OF_GUESTS_ERROR_MESSAGE);
        }
    }

    public Long getTableGroupIdOrNull() {
        if (isGroupTable()) {
            return tableGroup.getId();
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
