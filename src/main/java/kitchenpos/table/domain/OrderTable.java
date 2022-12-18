package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long id;
    @Column(name = "table_group_id", columnDefinition = "bigint(20)")
    private Long tableGroupId;
    @Column(nullable = false, columnDefinition = "int(11)")
    private int numberOfGuests;
    @Column(nullable = false, columnDefinition = "bit(1)")
    private boolean empty;
    @OneToOne(mappedBy = "orderTable", cascade = CascadeType.ALL)
    private Order order;

    public Order getOrder() {
        return order;
    }

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void ordered(Order order) {
        this.order = order;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGrouping() {
        return null != tableGroupId;
    }

    public void enGroupBy(Long tableGroupId) {
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void unGroupBy() {
        if (isGrouping()) {
            this.tableGroupId = null;
        }
    }

    public void changeNumberOfGuests(OrderTableValidator orderTableValidator, int numberOfGuests) {
        orderTableValidator.validateChangeNumberOfGuests(this, numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(OrderTableValidator orderTableValidator, boolean empty) {
        orderTableValidator.validateChangeEmpty(this);
        this.empty = empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderTable)) return false;
        OrderTable that = (OrderTable) o;
        return getNumberOfGuests() == that.getNumberOfGuests() && isEmpty() == that.isEmpty() && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumberOfGuests(), isEmpty());
    }
}
