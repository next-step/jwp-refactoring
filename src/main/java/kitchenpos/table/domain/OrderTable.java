package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    private static final String TABLE_THAT_CANNOT_BE_TABLE_GROUP = "단체 지정을 할 수 없는 테이블 입니다.";
    private static final String TABLE_IS_EMPTY = "테이블이 빈 테이블 상태 입니다.";
    private static final String ALREADY_EXISTS_GROUP_TABLE = "이미 단체 지정 된 테이블 입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;

    @Column(name = "table_status")
    private TableStatus tableStatus;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, TableStatus tableStatus) {
        this.numberOfGuests = numberOfGuests;
        this.tableStatus = tableStatus;
    }

    public void validateOrderTable() {
        if (!tableStatus.equals(TableStatus.EMPTY) || Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException(TABLE_THAT_CANNOT_BE_TABLE_GROUP);
        }
    }

    public void useTable() {
        this.tableStatus = TableStatus.ORDER;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public void validateTableGroupIsNull() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException(ALREADY_EXISTS_GROUP_TABLE);
        }
    }

    public void validateNotEmpty() {
        if (tableStatus.equals(TableStatus.EMPTY)) {
            throw new IllegalArgumentException(TABLE_IS_EMPTY);
        }
    }

    public void changeTableStatus(TableStatus tableStatus) {
        this.tableStatus = tableStatus;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long id() {
        return id;
    }

    public Long tableGroupId() {
        return tableGroupId;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }

    public TableStatus tableStatus() {
        return tableStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId) && tableStatus == that.tableStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, tableStatus);
    }
}
