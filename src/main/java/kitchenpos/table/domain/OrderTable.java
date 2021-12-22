package kitchenpos.table.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    @Enumerated(EnumType.STRING)
    private OrderTableStatus orderTableStatus = OrderTableStatus.EMPTY;

    public static OrderTable of(Long id, int numberOfGuests, OrderTableStatus orderTableStatus) {
        return new OrderTable(id, numberOfGuests, orderTableStatus);
    }

    public static OrderTable of(int numberOfGuests, OrderTableStatus orderTableStatus) {
        return new OrderTable(null, numberOfGuests, orderTableStatus);
    }

    private OrderTable(Long id, int numberOfGuests, OrderTableStatus orderTableStatus) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.orderTableStatus = orderTableStatus;
    }

    protected OrderTable() {
    }

    public void changeOrderTableStatus(boolean empty) {
        this.orderTableStatus = OrderTableStatus.valueOf(empty);
    }

    public void changeNumberOfGuest(int numberOfGuest) {
        this.numberOfGuests = numberOfGuest;
    }

    public void unGroup() {
        tableGroup = null;
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

    public boolean isEmpty() {
        return orderTableStatus.isEmpty();
    }

    public void withTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

}
