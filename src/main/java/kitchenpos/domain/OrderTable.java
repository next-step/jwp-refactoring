package kitchenpos.domain;

import kitchenpos.exception.TableEmptyUpdateException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @OneToOne(mappedBy = "orderTable", cascade = {CascadeType.ALL})
    private Order order;

    protected OrderTable() {
    }

    public OrderTable(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public void changeEmpty(boolean changeEmpty) {
        if (Objects.nonNull(tableGroup)) {
            throw new TableEmptyUpdateException();
        }

        if (Objects.nonNull(order) && !order.isCompleted()) {
            throw new TableEmptyUpdateException();
        }
        this.empty = changeEmpty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    public Order getOrder() {
        return order;
    }

    public boolean isOrderFinished() {
        return order.isCompleted();
    }

    public void updateNumberOfGuests(Integer newNumberOfGuests) {
        if (empty) {
            throw new TableEmptyUpdateException();
        }

        this.numberOfGuests = NumberOfGuests.of(newNumberOfGuests);
    }

    public OrderTable addOrder(Order order) {
        this.order = order;
        return this;
    }
}
