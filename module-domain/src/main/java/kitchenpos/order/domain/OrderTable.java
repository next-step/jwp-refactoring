package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.order.domain.OrderStatus.READY;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private Guests guests;

    @Embedded
    private TableState tableState;

    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    protected OrderTable() {
    }

    private OrderTable(int numberOfGuests, TableState tableState) {
        this.guests = new Guests(numberOfGuests);
        this.tableState = tableState;
        this.orderStatus = READY;
    }

    public static OrderTable of(int numberOfGuests, TableState tableState) {
        return new OrderTable(numberOfGuests, tableState);
    }

    public void group(Long newTableGroup) {
        this.tableGroupId = newTableGroup;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public void changeGuests(final int numberOfGuests) {
        this.guests = new Guests(numberOfGuests);
    }

    public void changeEmpty() {
        this.tableState = new TableState(true);
        this.guests = Guests.none();
    }

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isCompleted() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroupId)) {
            return null;
        }
        return tableGroupId;
    }

    public int getGuests() {
        return guests.getNumber();
    }

    public TableState getTableState() {
        return tableState;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
