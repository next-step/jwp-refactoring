package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.exception.ExistGroupTableException;
import kitchenpos.exception.InvalidGuestNumberException;
import kitchenpos.exception.NotExistException;
import kitchenpos.order.dto.OrderTableResponse;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column
    private int numberOfGuests;

    @Column
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public boolean existTableGroup() {
        return this.tableGroup != null;
    }

    public void validateGroupingTable() {
        if (this.tableGroup != null) {
            throw new ExistGroupTableException();
        }
    }

    public void mappingTableGroupId(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public void empty() {
        this.empty = true;
    }

    public void notEmpty() {
        this.empty = false;
    }

    public void changeGuestNumber(int guestNumber) {
        validateEmpty();
        validateGuestNumber(guestNumber);
        this.numberOfGuests = guestNumber;
    }

    public void ungroupTable() {
        this.tableGroup = null;
    }

    private void validateEmpty() {
        if (this.empty) {
            throw new NotExistException("비어있는 테이블입니다.");
        }
    }

    private void validateGuestNumber(int guestNumber) {
        if (guestNumber < 1) {
            throw new InvalidGuestNumberException();
        }
    }

    public OrderTableResponse toOrderTableResponse() {
        if (this.tableGroup == null) {
            return new OrderTableResponse(this.id, null, this.numberOfGuests, this.empty);
        }
        return new OrderTableResponse(this.id, this.tableGroup.getId(), this.numberOfGuests, this.empty);
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
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id)
                && Objects.equals(tableGroup, that.tableGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty);
    }
}
