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
    private static final int EMPTY_GEUST = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer numberOfGuests;
    @Column(nullable = false)
    private Boolean empty;
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Embedded
    private Orders orders = new Orders();

    protected OrderTable() {
    }

    public OrderTable(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void registerOrder(Order order) {
        if (Boolean.TRUE.equals(empty)) {
            throw new IllegalArgumentException("[ERROR] 빈테이블에는 주문등록을 할 수 없습니다.");
        }
        orders.addOrder(order);
        order.setOrderTable(this);
    }

    public void ungroupingTableGroup() {
        orders.checkPossibleUngroupingOrderStatus();
        if (tableGroup == null) {
            throw new IllegalArgumentException("[ERROR] 단체 지정이 되어있지 않아 해제할 수 없습니다.");
        }
        tableGroup = null;
    }

    public void assignTableGroup(TableGroup tableGroup) {
        checkPossibleGrouping();
        this.tableGroup = tableGroup;
        empty = false;
    }

    private void checkPossibleGrouping() {
        if (tableGroup != null) {
            throw new IllegalArgumentException("[ERROR] 이미 단체지정이 되어있습니다.");
        }
        if (Boolean.FALSE.equals(empty)) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블이 아닌 경우 단체 지정 할 수 없습니다.");
        }
    }

    public void checkPossibleChangeEmpty() {
        if (tableGroup != null) {
            throw new IllegalArgumentException("[ERROR] 단체 지정이 되어있어 업데이트 할 수 없습니다.");
        }
        orders.checkPossibleChangeEmpty();
    }

    public void updateNumberOfGuests(Integer numberOfGuests) {
        if (numberOfGuests < EMPTY_GEUST) {
            throw new IllegalArgumentException("[ERROR] 방문 손님 수는 0명 미만으로 변경할 수 없습니다.");
        }
        if (Boolean.TRUE.equals(empty)) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블은 방문 손님 수를 변경할 수 없습니다.");
        }
        orders.checkPossibleChangeEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    public void updateEmpty(boolean empty) {
        this.empty = empty;
    }

    public void addOrder(Order order) {
        orders.addOrder(order);
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }
}
