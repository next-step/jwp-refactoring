package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {

    public static final String MESSAGE_VALIDATE_ORDER_TABLE_CHANGABLE = "테이블이 비어있지 않아야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    @Column(nullable = false)
    private NumberOfGuests numberOfGuests;

    @Embedded
    @Column(nullable = false)
    private Empty empty;

    protected OrderTable() {
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new Empty(empty);
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new Empty(empty);
    }

    public OrderTable(boolean empty) {
        this.empty = new Empty(empty);
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty, OrderTableValidator orderTableValidator) {
        orderTableValidator.validateEmptyChangable(this);
        this.empty = new Empty(empty);
    }

    public void changeNumberOfGuests(int numberOfGuests, OrderTableValidator orderTableValidator) {
        orderTableValidator.validateNumberOfGuestsChangable(empty);
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        tableGroupId = null;
    }

    public boolean isGroupable() {
        return empty.isGroupable() && Objects.isNull(tableGroupId);
    }

    public void validateOrderTableChangable() {
        if (empty.isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_ORDER_TABLE_CHANGABLE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId) && Objects.equals(numberOfGuests, that.numberOfGuests) && Objects.equals(empty, that.empty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
