package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.exception.EmptyTableException;
import kitchenpos.common.exception.ExistGroupTableException;
import kitchenpos.common.exception.NotEmptyException;
import kitchenpos.common.exception.NotExistException;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.event.ChangeEmptyTableEventPublisher;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class OrderTable extends AbstractAggregateRoot<OrderTable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tableGroupId;

    @Embedded
    private GuestNumber guestNumber;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    OrderTable(Builder builder) {
        this.id = builder.id;
        this.tableGroupId = builder.tableGroupId;
        this.guestNumber = builder.guestNumber;
        this.empty = builder.empty;
    }

    public Long getId() {
        return id;
    }

    public void validateExistGroupingTable() {
        if (this.tableGroupId != null) {
            throw new ExistGroupTableException();
        }
    }

    public void validateGrouping() {
        validateExistGroupingTable();
        if (!this.empty) {
            throw new NotEmptyException();
        }
    }

    public void validateEmpty() {
        if (this.empty) {
            throw new EmptyTableException();
        }
    }

    public void changeEmpty() {
        registerEvent(new ChangeEmptyTableEventPublisher(this));
        this.empty = true;
    }

    public void changeGuestNumber(int guestNumber) {
        if (this.empty) {
            throw new NotExistException("비어있는 테이블입니다.");
        }
        this.guestNumber = GuestNumber.of(guestNumber);
    }

    public void ungroupTable() {
        this.tableGroupId = null;
    }

    public OrderTableResponse toOrderTableResponse() {
        if (this.tableGroupId == null) {
            return new OrderTableResponse(this.id, null, this.guestNumber.number(), this.empty);
        }
        return new OrderTableResponse(this.id, this.tableGroupId, this.guestNumber.number(), this.empty);
    }

    boolean isEmpty() {
        return this.empty;
    }

    GuestNumber guestNumber() {
        return this.guestNumber;
    }

    Long tableGroupId() {
        return this.tableGroupId;
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
        return empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId,
                that.tableGroupId) && Objects.equals(guestNumber, that.guestNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, guestNumber, empty);
    }

    public static class Builder {
        private Long id;
        private Long tableGroupId;
        private GuestNumber guestNumber;
        private boolean empty;

        public Builder() {
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setTableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
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
