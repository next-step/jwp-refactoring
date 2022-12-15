package kitchenpos.domain.order;

import kitchenpos.domain.product.TableGroup;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private Orders orders = Orders.createEmpty();

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = Optional.ofNullable(tableGroupId)
                .map(it -> TableGroup.of(it, LocalDateTime.now()))
                .orElse(null);
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, null, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return Optional.ofNullable(tableGroup)
                .map(TableGroup::getId)
                .orElse(null);
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroup = Optional.ofNullable(tableGroupId)
                .map(it -> TableGroup.of(it, LocalDateTime.now()))
                .orElse(null);
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }


    public boolean isEmpty() {
        return empty;
    }
    public Orders getOrders() {
        return orders;
    }

    public void registerTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void unGroup() {
        this.tableGroup = null;
    }
}
