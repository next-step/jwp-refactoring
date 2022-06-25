package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.common.Empty;
import kitchenpos.domain.common.NumberOfGuests;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, boolean empty) {
        this.id = id;
        this.empty = new Empty(empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.empty = new Empty(empty);
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new Empty(empty);
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new Empty(empty);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void attachToTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        if (!isEmpty()) {
            throw new IllegalArgumentException("손님수를 변경 할 수 없습니다. 빈 테이블이 아닙니다.");
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty.isTrue();
    }

    public void switchEmpty(boolean empty) {
        if (isInTableGroup()){
            throw new IllegalArgumentException("빈테이블로 지정 할 수 없습니다. 단체 지정이 되어 있는 주문 테이블 입니다.");
        }
        this.empty = new Empty(empty);
    }

    public boolean isInTableGroup() {
        return tableGroup != null;
    }

    public void detachFromTableGroup() {
        this.tableGroup = null;
    }
}
