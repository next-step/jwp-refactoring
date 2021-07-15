package kitchenpos.ordertable.domain;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.generic.exception.IllegalOperationException;
import kitchenpos.generic.guests.domain.NumberOfGuests;
import kitchenpos.tablegroup.domain.TableGroup;

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

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeEmpty(boolean empty) {
        checkOrders();
        checkNotGrouped();
        this.empty = empty;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        checkNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    public void leaveTableGroup() {
        this.tableGroupId = null;
    }

    private void checkOrders() {
        // if (orders.hasOrderInProgress()) { // TODO 나중에 확인!
        //     throw new OrderNotCompletedException("테이블에 완결되지 않은 주문이 존재합니다.");
        // }
    }

    private void checkNotGrouped() {
        // if (this.hasTableGroup()) { // TODO 나중에 확인!
        //     throw new IllegalArgumentException("테이블 그룹에 포함되어 있습니다.");
        // }
    }

    private void checkNotEmpty() {
        if (this.isEmpty()) {
            throw new IllegalOperationException("빈 테이블 입니다.");
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGroupable() {
        return isEmpty() && !getTableGroupId().isPresent();
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
