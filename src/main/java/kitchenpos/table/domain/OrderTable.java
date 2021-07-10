package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    private static final String TABLE_THAT_CANNOT_BE_TABLE_GROUP = "단체 지정을 할 수 없는 테이블 입니다.";
    private static final String TABLE_IS_EMPTY = "테이블은 빈 테이블 상태 입니다.";
    private static final String ALREADY_EXISTS_GROUP_TABLE = "이미 단체 지정 된 테이블 입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void validateOrderTable() {
        if (!empty || Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException(TABLE_THAT_CANNOT_BE_TABLE_GROUP);
        }
    }

    public void mappingTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public void validateTableGroupIsNull() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException(ALREADY_EXISTS_GROUP_TABLE);
        }
    }

    public void validateNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException(TABLE_IS_EMPTY);
        }
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long id() {
        return id;
    }

    public TableGroup tableGroup() {
        return tableGroup;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroup, that.tableGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty);
    }
}
