package kitchenpos.table.domain;

import javax.persistence.*;

@Entity
public class OrderTable {
    public static final String CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE = "변경하는 손님수는 0명보다 작을 수 없습니다.";
    public static final int CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER = 0;
    public static final String TABLE_GROUP_EMPTY_EXCEPTION_MESSAGE = "테이블 그룹이 존재하지 않습니다.";
    public static final String NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE = "0명보다 작을 수 없다.";
    public static final String EMPTY_EXCEPTION_MESSAGE = "공석일 경우 손님수를 변경할 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
        this.empty = false;
    }

    public OrderTable(boolean empty) {
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(TableGroup tableGroup, boolean empty) {
        this.tableGroup = tableGroup;
        this.empty = empty;
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

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void empty() {
//        if (this.tableGroup == null) {
//            throw new IllegalArgumentException(TABLE_GROUP_EMPTY_EXCEPTION_MESSAGE);
//        }
        this.empty = true;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER) {
            throw new IllegalArgumentException(CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE);
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE);
        }
//        if (Objects.isNull(tableGroup)) {
//            throw new IllegalArgumentException(ORDER_TABLE_NULL_EXCEPTION_MESSAGE);
//        }
        if (isEmpty()) {
            throw new IllegalArgumentException(EMPTY_EXCEPTION_MESSAGE);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return this.tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }
}
