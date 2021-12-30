package kitchenpos.domain;

import kitchenpos.common.exceptions.NegativeNumberOfGuestsException;
import kitchenpos.common.exceptions.NotEmptyOrderTableGroupException;

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
    @Column(nullable = false)
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    protected OrderTable() {}

    private OrderTable(final Long id, final TableGroup tableGroup, final NumberOfGuests numberOfGuests, final Empty empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }


    public static OrderTable of(final Long id, final TableGroup tableGroup, final NumberOfGuests numberOfGuests, final Empty empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable of(final NumberOfGuests numberOfGuests, final Empty empty) {
        return of(null, null, numberOfGuests, empty);
    }

    public static OrderTable of(final int numberOfGuests, final boolean empty) {
        return of(null, null, NumberOfGuests.from(numberOfGuests), Empty.from(empty));
    }

    public static OrderTable of(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        return of(null, tableGroup, NumberOfGuests.from(numberOfGuests), Empty.from(empty));
    }

    public static OrderTable of(final Long id, final int numberOfGuests, final boolean empty) {
        return new OrderTable(id, null, NumberOfGuests.from(numberOfGuests), Empty.from(empty));
    }

    public void changeEmptyStatus(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new NotEmptyOrderTableGroupException();
        }
        this.empty = Empty.from(empty);
    }

    public void changeNumberOfGuests(final int numbers) {
        if (this.getEmpty().isEmpty()) {
            throw new NegativeNumberOfGuestsException();
        }
        this.numberOfGuests = NumberOfGuests.from(numbers);
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public Empty getEmpty() {
        return empty;
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
