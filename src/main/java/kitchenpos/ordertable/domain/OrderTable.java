package kitchenpos.ordertable.domain;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.generic.guests.domain.NumberOfGuests;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Column(nullable = false)
    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void groupedBy(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void changeEmpty(OrderTableValidator validator, boolean empty) {
        validator.validateChangeTableStatus(this);
        this.empty = empty;
    }

    public void changeNumberOfGuests(OrderTableValidator validator, NumberOfGuests numberOfGuests) {
        validator.validateChangeNumberOfGuests(this);
        this.numberOfGuests = numberOfGuests;
    }

    public void leaveTableGroup() {
        this.tableGroupId = null;
    }

    public boolean isGroupable() {
        return isEmpty() && !hasTableGroup();
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean hasTableGroup() {
        return getTableGroupId().isPresent();
    }

    public Long getId() {
        return id;
    }

    public Optional<Long> getTableGroupId() {
        return Optional.ofNullable(tableGroupId);
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderTable that = (OrderTable)o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
