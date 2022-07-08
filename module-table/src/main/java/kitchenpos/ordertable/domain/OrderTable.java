package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.exception.IllegalOrderTableException;

import javax.persistence.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "table_group_id")
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public static final String ERROR_ORDER_TABLE_EMPTY = "주문테이블은 비어있을 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_GROUPED = "주문테이블은 그룹에 지정되어있을 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_GUESTS_TOO_SMALL = "주문테이블의 손님수는 %d 미만일 수 없습니다.";
    public static final int MIN_NUMBER_OF_GUESTS = 0;

    protected OrderTable() {
    }

    private OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public void changeEmpty(boolean empty) {
        validateOrderTableNotGrouped();
        this.empty = empty;
    }

    private void validateOrderTableNotGrouped() {
        if (isGrouped()) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_GROUPED);
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateOrderTableNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalOrderTableException(
                    String.format(ERROR_ORDER_TABLE_GUESTS_TOO_SMALL, MIN_NUMBER_OF_GUESTS)
            );
        }
    }

    private void validateOrderTableNotEmpty() {
        if (isEmpty()) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_EMPTY);
        }
    }

    public void assignTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void removeTableGroup() {
        this.tableGroupId = null;
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

    public boolean isGrouped() {
        return tableGroupId != null;
    }
}
