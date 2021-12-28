package kitchenpos.table.domain;

import kitchenpos.table.application.TableValidator;

import javax.persistence.Column;
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

    @Column
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, Empty empty) {
        this.id = id;
        this.empty = empty;
    }

    private OrderTable(NumberOfGuests numberOfGuests, Empty empty) {

        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests, Empty empty) {
        this(numberOfGuests, empty);
        this.id = id;
        this.tableGroupId = tableGroupId;
    }

    public static OrderTable of(Long id, Empty empty) {
        return new OrderTable(id, empty);
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
        if (tableGroupId == null) {
            return null;
        }
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        tableGroupId = null;
    }

    public void changeEmpty(Empty empty, TableValidator tableValidator) {
        tableValidator.validateChangeEmpty(this);
        this.empty = empty;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests, TableValidator tableValidator) {
        tableValidator.validateChangeNumberOfGuests(this);
        this.numberOfGuests = numberOfGuests;
    }
}
