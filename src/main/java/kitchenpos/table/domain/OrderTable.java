package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private NumberOfGuest numberOfGuests;
    @Column(nullable = false)
    private TableEmpty empty;
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Embedded
    private Orders orders = new Orders();

    protected OrderTable() {
    }

    public OrderTable(Long id, NumberOfGuest numberOfGuests, TableEmpty empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void registerOrder(Order order) {
        validateOrderTable();
        orders.registerOrder(order);
        order.setOrderTable(this);
    }

    private void validateOrderTable() {
        if (empty.isEmptyTable()) {
            throw new IllegalArgumentException("[ERROR] 빈테이블에는 주문등록을 할 수 없습니다.");
        }
    }

    public void ungroupingTableGroup() {
        orders.checkPossibleUngroupingOrderStatus();
        validateTableGroup();
        tableGroup = null;
    }

    private void validateTableGroup() {
        if (tableGroup == null) {
            throw new IllegalArgumentException("[ERROR] 단체 지정이 되어있지 않아 해제할 수 없습니다.");
        }
    }

    public void assignTableGroup(TableGroup tableGroup) {
        checkPossibleGrouping();
        this.tableGroup = tableGroup;
        empty.changeOrderTable();
    }

    private void checkPossibleGrouping() {
        if (tableGroup != null) {
            throw new IllegalArgumentException("[ERROR] 이미 단체지정이 되어있습니다.");
        }
        if (empty.isOrderTable()) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블이 아닌 경우 단체 지정 할 수 없습니다.");
        }
    }

    public void checkPossibleChangeEmpty() {
        if (tableGroup != null) {
            throw new IllegalArgumentException("[ERROR] 단체 지정이 되어있어 업데이트 할 수 없습니다.");
        }
        orders.checkPossibleChangeEmpty();
    }

    public void updateNumberOfGuests(NumberOfGuest numberOfGuests) {
        validateUpdateNumberOfGuests();
        orders.checkPossibleChangeEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateUpdateNumberOfGuests() {
        if (empty.isEmptyTable()) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블은 방문 손님 수를 변경할 수 없습니다.");
        }
    }

    public boolean isEmptyTable() {
        return empty.isEmpty();
    }

    public void updateEmpty(TableEmpty empty) {
        this.empty = empty;
    }

    public void addOrder(Order order) {
        orders.addOrder(order);
    }

    public Long getId() {
        return id;
    }

    public NumberOfGuest getNumberOfGuests() {
        return numberOfGuests;
    }

    public TableEmpty getEmpty() {
        return empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }
}
