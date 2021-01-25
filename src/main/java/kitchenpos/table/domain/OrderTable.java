package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.tableGroup.domain.TableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    @OneToOne(mappedBy = "orderTable")
    private Order order;

    protected OrderTable() {
    }

    public static OrderTable empty() {
        return new OrderTable(TableGroup.empty());
    }

    public OrderTable(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        removeTableGroup();
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        removeTableGroup();
    }

    public void removeTableGroup() {
        if(Objects.nonNull(tableGroup) && tableGroup.hasContain(this)) {
            tableGroup.removeTable(this);
        }
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

    public Order getOrder() {
        return order;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeStatus(boolean empty) {
        checkGrouping();
        if(Objects.nonNull(this.order) && !order.checkComplete()) {
            throw new IllegalArgumentException("주문이 완료되지 않았습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        checkEmpty();
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수가 올바르지 않습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void addTableGroup(TableGroup tableGroup) {
        if(!tableGroup.hasContain(this)) {
            tableGroup.addTable(this);
        }
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void checkOrderTable() {
        checkGrouping();
        checkNotEmpty();
    }

    public void checkEmpty() {
        if (empty) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
        }
    }

    public void checkNotEmpty() {
        if (!empty) {
            throw new IllegalArgumentException("테이블이 사용중입니다.");
        }
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    private void checkGrouping() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("그룹핑된 상태입니다.");
        }
    }
}
