package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Embedded;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = Empty.of(empty);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateTableNotEmpty();

        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public void changeEmpty() {
        validateHasNotTableGroup();

        this.empty = Empty.of(true);
    }

    public void makeGroup(final TableGroup tableGroup) {
        validateGroupingPossible();

        this.tableGroup = tableGroup;
        this.empty = Empty.of(false);
    }

    public void ungroup() {
        validateUngroupPossible();

        this.tableGroup = null;
    }

    private void validateUngroupPossible() {
        if (Objects.isNull(tableGroup)) {
            throw new IllegalArgumentException("단체로 지정되어 있지 않습니다.");
        }
    }

    private void validateGroupingPossible() {
        validateTableGroupIsNull();
        validateTableIsEmpty();
    }

    private void validateTableGroupIsNull() {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("이미 단체로 지정되어 있습니다.");
        }
    }

    private void validateTableIsEmpty() {
        if (!empty.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어 있지 않습니다.");
        }
    }

    private void validateHasNotTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체로 지정된 테이블은 빈 테이블로 변경할 수 없습니다.");
        }
    }

    private void validateTableNotEmpty() {
        if (empty.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블의 손님 수를 변경할 수 없습니다.");
        }
    }
}
