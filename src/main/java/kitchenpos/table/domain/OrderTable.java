package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.exception.ExistGroupTableException;
import kitchenpos.exception.NotEmptyException;
import kitchenpos.exception.NotExistException;
import kitchenpos.order.dto.OrderTableResponse;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private GuestNumber guestNumber;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    OrderTable(Builder builder) {
        this.id = builder.id;
        this.tableGroup = builder.tableGroup;
        this.guestNumber = builder.guestNumber;
        this.empty = builder.empty;
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

    public void validateGrouping() {
        validateGroupingTable();
        if (!this.empty) {
            throw new NotEmptyException();
        }
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public void empty() {
        this.empty = true;
    }

    public void changeGuestNumber(int guestNumber) {
        if (this.empty) {
            throw new NotExistException("비어있는 테이블입니다.");
        }
        this.guestNumber = GuestNumber.of(guestNumber);
    }

    public void ungroupTable() {
        this.tableGroup = null;
    }

    public OrderTableResponse toOrderTableResponse() {
        if (this.tableGroup == null) {
            return new OrderTableResponse(this.id, null, this.guestNumber.number(), this.empty);
        }
        return new OrderTableResponse(this.id, this.tableGroup.getId(), this.guestNumber.number(), this.empty);
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
        return empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroup,
                that.tableGroup) && Objects.equals(guestNumber, that.guestNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, guestNumber, empty);
    }

    public static class Builder {
        private Long id;
        private TableGroup tableGroup;
        private GuestNumber guestNumber;
        private boolean empty;

        public Builder() {
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setTableGroup(TableGroup tableGroup) {
            this.tableGroup = tableGroup;
            return this;
        }

        public Builder setGuestNumber(GuestNumber guestNumber) {
            this.guestNumber = guestNumber;
            return this;
        }

        public Builder setEmpty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(this);
        }
    }
}
