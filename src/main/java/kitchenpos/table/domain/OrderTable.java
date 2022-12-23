package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderValidator;

import javax.persistence.*;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {}

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable create(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void update(TableGroup tableGroup, boolean empty) {
        this.tableGroup = tableGroup;
        this.empty = empty;
    }

    public void removeTableGroup() {
        this.tableGroup = null;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        OrderValidator.validateEmpty(empty);
        this.numberOfGuests = numberOfGuests;
    }

    public void updateEmpty(final boolean empty) {
        OrderValidator.validateGrouped(tableGroup);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (tableGroup != null) {
            return tableGroup.getId();
        }
        return null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

}
