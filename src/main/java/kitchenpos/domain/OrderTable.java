package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Orders orders = new Orders();

    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Void validateAlreadyTableGroup() {
        if (Objects.nonNull(getTableGroupId())) {
            throw new IllegalArgumentException();
        }
        return null;
    }

    public Void validateOrderStatus(List<String> orderStatuses) {
        orders.validateOrderStatus(orderStatuses);
        return null;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void addOrder(Order order) {
        orders.addOrder(order);
    }

    public Void validateEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
        return null;
    }

    public Void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
