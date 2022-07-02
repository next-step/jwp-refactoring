package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public static final String ERROR_ORDER_TABLE_EMPTY = "주문테이블은 비어있을 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_GROUPED = "주문테이블은 그룹에 지정되어있을 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_GUESTS_TOO_SMALL = "주문테이블의 손님수는 %d 미만일 수 없습니다.";
    public static final int MIN_NUMBER_OF_GUESTS = 0;

    protected OrderTable() {
    }

    private OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
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

    public void assignTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void removeTableGroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGrouped() {
        return tableGroup != null;
    }
}
