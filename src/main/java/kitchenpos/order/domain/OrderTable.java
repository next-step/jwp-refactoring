package kitchenpos.order.domain;

import javax.persistence.*;

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
        this.tableState = new TableState(true);
    }

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
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
