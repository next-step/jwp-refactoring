package kitchenpos.ordertable.domain;

import kitchenpos.ordertablegroup.domain.OrderTableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "order_table")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private OrderTableGroup orderTableGroup;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(OrderTableGroup orderTableGroup, int numberOfGuests, boolean empty) {
        this.orderTableGroup = orderTableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public OrderTableGroup getOrderTableGroup() {
        return orderTableGroup;
    }

    public void setOrderTableGroup(OrderTableGroup orderTableGroup) {
        this.orderTableGroup = orderTableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void ungroupTable() {
        this.orderTableGroup = null;
    }

    public boolean isNotAvailableOrderTable() {
        return Objects.nonNull(this.getOrderTableGroup());
    }

    public Long getTableGroupId() {
        return orderTableGroup == null ? null : orderTableGroup.getId();
    }
}
