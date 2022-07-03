package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.order.domain.Order;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

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

    public OrderTable(Long id, Long tableGroupId, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.empty = empty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void attachToTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
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
        this.empty = true;
    }

    public void detachFromTableGroup() {
        this.tableGroupId = null;
    }

    private void validateOrderTablesStatus(Order order) {
        if (!order.checkOrderComplete()) {
            throw new IllegalArgumentException("주문 상태가 COMPLETION이 아닙니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return this.tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }


    public boolean isEmptyTable() {
        return empty;
    }

    public boolean isInTableGroup() {
        return this.tableGroupId != null;
    }
}
