package kitchenpos.table.domain;

import javax.persistence.*;

@Entity
@Table
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long tableGroupId;

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

    private OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests, Empty empty) {
        this(numberOfGuests, empty);

        this.id = id;
        this.tableGroupId = tableGroupId;
    }

    public static OrderTable of(NumberOfGuests numberOfGuests, Empty empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable of(Long id, Long tableGroupId, NumberOfGuests numberOfGuests, Empty empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
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

    public boolean isNotEmpty() {
        return !empty.isEmpty();
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }
}
