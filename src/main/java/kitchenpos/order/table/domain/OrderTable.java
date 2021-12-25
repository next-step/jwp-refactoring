package kitchenpos.order.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.table.dto.OrderTableRequest;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "order_table")
@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "orderTable")
    private Order order;

    public OrderTable() {

    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable create(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public Order getOrder() {
        return order;
    }

    private boolean isGrouping() {
        return Objects.nonNull(this.tableGroup);
    }

    public void validateTableGrouping() {
        if (!isEmpty() || isGrouping()) {
            throw new IllegalArgumentException();
        }
    }

    public void empty() {
        this.empty = true;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public void notEmpty(TableGroup tableGroup) {
        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public void order(Order order) {
        this.order = order;
    }
}
