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

    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = Empty.of(empty);
    }

    public OrderTable(final Long id, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = Empty.of(empty);
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = Empty.of(empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests,
        final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = Empty.of(empty);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public void setEmpty(final boolean empty) {
        this.empty = Empty.of(empty);
    }

    public void changeEmpty() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체로 지정된 테이블은 빈 테이블로 변경할 수 없습니다.");
        }

        this.empty = Empty.of(true);
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if(empty.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블의 손님 수를 변경할 수 없습니다.");
        }

        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }
}
