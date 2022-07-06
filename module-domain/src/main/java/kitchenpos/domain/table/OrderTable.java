package kitchenpos.domain.table;

import java.util.Objects;
import javax.persistence.*;

@Entity
public class OrderTable {
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

    public OrderTable() {}

    private OrderTable(int numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void unGroupTable() {
        this.tableGroup = null;
    }

    public void registerGroupTable(TableGroup tableGroup) {
        validateGroupAndEmpty();
        this.tableGroup = tableGroup;
    }

    public boolean hasGroup() {
        return Objects.nonNull(tableGroup);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void updateNumberOfGuests(OrderTable updateTable) {
        this.numberOfGuests = updateTable.numberOfGuests;
    }

    public void updateEmpty(OrderTable updateTable) {
        this.empty = updateTable.empty;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님수는 0 이상이어야 합니다.");
        }
    }

    private void validateGroupAndEmpty() {
        if (hasGroup()) {
            throw new IllegalArgumentException("이미 단체 지정이 되어있습니다.");
        }
        if (!isEmpty()) {
            throw new IllegalArgumentException("단체 지정은 빈 테이블이어야 합니다.");
        }
    }
}
