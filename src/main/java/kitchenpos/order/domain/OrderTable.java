package kitchenpos.order.domain;

import kitchenpos.tableGroup.domain.TableGroup;

import javax.persistence.*;
import java.util.List;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @Embedded
    private Orders orders;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = new Orders();
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateNumber(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumber(int numberOfGuests) {
        if (numberOfGuests < 0 || empty) {
            throw new IllegalArgumentException();
        }
    }

    public void changeEmpty(final boolean empty) {
        validateCompletion();
        this.empty = empty;
    }

    private void validateCompletion() {
        orders.validateCompletion();
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void addOrders(List<Order> addOrders) {
        addOrders.stream()
                .forEach(order -> {
                    order.updateOrderTable(this);
                    this.orders.add(order);
                });
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
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

    public boolean isEmpty() {
        return empty;
    }

    public List<Order> getOrders() {
        return orders.getOrders();
    }

}
