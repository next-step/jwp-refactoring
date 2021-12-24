package kitchenpos.order.domain;

import kitchenpos.order.application.exception.InvalidOrderState;
import kitchenpos.order.application.exception.InvalidTableState;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.order.domain.OrderStatus.READY;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    private int numberOfGuests;

    @Embedded
    private TableState tableState;

    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    protected OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests, TableState tableState) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.tableState = tableState;
        this.orderStatus = READY;
    }

    public OrderTable(int numberOfGuests, TableState tableState) {
        this(null, numberOfGuests, tableState);
    }

    public boolean isCompleted() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public void setTableGroup(TableGroup tableGroup) {
        if (this.tableGroup != null) {
            this.tableGroup.getOrderTables().remove(this);
        }
        this.tableGroup = tableGroup;
        tableGroup.getOrderTables().add(this);
    }

    public void changeEmpty() {
        if (Objects.nonNull(tableGroup)) {
            throw new InvalidTableState("테이블에 일행이 있습니다.");
        }

        if (!orderStatus.isCompleted()) {
            throw new InvalidOrderState("주문 상태가 완료되지 않아 테이블을 정리할 수 없습니다.");
        }

        this.tableState = new TableState(true);
        this.numberOfGuests = 0;
    }

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void changeGuests(final int numberOfGuests) {
        if (tableState.isEmpty()) {
            throw new InvalidTableState("빈 테이블 입니다.");
        }
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

    public TableState getTableState() {
        return tableState;
    }

    public boolean isEmpty() {
        return tableState.isEmpty();
    }
}
