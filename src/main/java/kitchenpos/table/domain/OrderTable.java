package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;
import java.util.Objects;

import static kitchenpos.common.Messages.HAS_ORDER_TABLE_GROUP;

@Entity
@Table
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    protected OrderTable() {
    }

    private OrderTable(NumberOfGuests numberOfGuests, Empty empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, Empty empty) {
        this(numberOfGuests, empty);

        this.id = id;
        this.tableGroup = tableGroup;
    }

    public static OrderTable of(NumberOfGuests numberOfGuests, Empty empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable of(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, Empty empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }

        return tableGroup.getId();
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public void setNumberOfGuests(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }

    public boolean isNotEmpty() {
        return !empty.isEmpty();
    }

    public void validateHasOrderTable() {
        if (hasOrderTableGroup()) {
            throw new IllegalArgumentException(HAS_ORDER_TABLE_GROUP);
        }
    }

    public boolean hasOrderTableGroup() {
        return Objects.nonNull(getTableGroupId());
    }
}
