package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.common.domain.NumberOfGuests;

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

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, boolean empty) {
        this.id = id;
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.empty = empty;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void attachToTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        if (!this.empty) {
            throw new IllegalArgumentException("손님수를 변경 할 수 없습니다. 빈 테이블이 아닙니다.");
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void clear(Order order) {
        if (this.empty) {
            return;
        }
        validateOrderTablesStatus(order);
        if (isInTableGroup()) {
            throw new IllegalArgumentException("빈테이블로 지정 할 수 없습니다. 단체 지정이 되어 있는 주문 테이블 입니다.");
        }
        this.empty = true;
    }

    public boolean isInTableGroup() {
        return tableGroup != null;
    }

    public void detachFromTableGroup() {
        this.tableGroup = null;
    }

    private void validateOrderTablesStatus(Order order) {
        if (!order.checkOrderComplete()) {
            throw new IllegalArgumentException("주문 상태가 COMPLETION이 아닙니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }


    public boolean isEmpty() {
        return empty;
    }
}
