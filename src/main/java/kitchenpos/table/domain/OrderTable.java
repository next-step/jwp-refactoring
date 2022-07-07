package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class OrderTable {
    private static final String ERROR_MESSAGE_ALREADY_GROUPED = "이미 단체지정된 테이블입니다.";
    private static final String ERROR_MESSAGE_EMPTY_TABLE = "빈 테이블은 방문객 수를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Column(nullable = false)
    private int numberOfGuests;
    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void validateHasTableGroupId() {
        if (isGrouped()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ALREADY_GROUPED);
        }
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroup);
    }

    public void validateIsEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_EMPTY_TABLE);
        }
    }

    public void updateEmpty() {
        this.empty = true;
    }

    public Long getTableGroupId() {
        if (tableGroup != null) {
            return tableGroup.getId();
        }
        return null;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
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
}
