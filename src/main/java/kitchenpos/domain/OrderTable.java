package kitchenpos.domain;

import kitchenpos.exception.CannotChangeTableEmptyException;
import kitchenpos.exception.TableGroupNotAvailableException;
import kitchenpos.exception.TableNotAvailableException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class OrderTable extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @Column(name = "number_of_guests", length = 11, nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", length = 1, nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void addTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty=false;
    }

    public void checkAvailability() {
        if (this.empty) {
            throw new TableNotAvailableException(String.format("table id is %d", this.id));
        }
    }

    public void checkAvailabilityTableGroup() {
        if (!this.empty || Objects.nonNull(this.tableGroup)) {
            throw new TableGroupNotAvailableException(String.format("table id is %d", this.id));
        }
    }

    public void initTableGroup() {
        this.tableGroup = null;
    }

    public void changeEmpty(boolean empty) {
        if (this.getTableGroup() != null) {
            throw new CannotChangeTableEmptyException(String.format("table id is %d", this.id));
        }
        checkOrderStatus();

        this.empty = empty;
    }

    private void checkOrderStatus() {
        this.orders.stream()
                .map(Order::getOrderStatus)
                .filter(orderStatus -> orderStatus.equals(OrderStatus.COOKING) || orderStatus.equals(OrderStatus.MEAL))
                .findFirst()
                .ifPresent(orderStatus -> {
                    throw new IllegalArgumentException();
                });
    }

    public void addOrder(final Order order) {
        this.orders.add(order);
    }

    public void changeNumberOfGuest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        checkOrderStatus();
        this.tableGroup = null;
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
        return empty;
    }

}
