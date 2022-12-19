package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    public static final String CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE = "변경하는 손님수는 0명보다 작을 수 없습니다.";
    public static final String TABLE_GROUP_NOT_NULL_EXCEPTION_MESSAGE = "테이블 그룹이 존재하지 않습니다.";
    public static final String EMPTY_EXCEPTION_MESSAGE = "공석일 경우 손님수를 변경할 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    public OrderTable(TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public void empty() {
        validateNotNullTableGroup();
        this.empty = true;
    }

    public void changeSitNumberOfGuest(NumberOfGuests numberOfGuests) {
        validateEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return this.tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return this.numberOfGuests;
    }

    private void validateNotNullTableGroup() {
        if (!Objects.isNull(this.tableGroup)) {
            throw new IllegalArgumentException(TABLE_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
        }
    }

    private void validateEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException(EMPTY_EXCEPTION_MESSAGE);
        }
    }
}
