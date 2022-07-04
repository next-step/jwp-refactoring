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

    public OrderTable() {

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

    private boolean isGroupTable() {
        return tableGroup != null;
    }

    public void validateChangeOrderTableNumberOfGuests() {
        validateNumberOfGuestsOverZero();
        validateEmptyOrderTable();
    }

    private void validateNumberOfGuestsOverZero() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmptyOrderTable() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }


    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getTableGroupIdOrNull() {
        if (isGroupTable()) {
            return tableGroup.getId();
        }
        return null;
    }
}
