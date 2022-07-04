package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.Exception.OrderTableAlreadyEmptyException;
import kitchenpos.Exception.OrderTableAlreadyTableGroupException;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class OrderTable extends AbstractAggregateRoot<OrderTable> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long tableGroupId;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Column
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        validateTableGroup();
        this.empty = empty;
        registerEvent(new OrderTableChangedEmptyEvent(id));
    }


    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        validateEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    public void groupByTableGroupId(Long tableGroupId) {
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    private void validateTableGroup() {
        if (tableGroupId != null) {
            throw new OrderTableAlreadyTableGroupException("단체 지정인 테이블은 빈 테이블로 지정할 수 없습니다.");
        }
    }

    private void validateEmpty() {
        if (isEmpty()) {
            throw new OrderTableAlreadyEmptyException("빈 테이블인 경우에는 손님을 받을 수 없습니다.");
        }
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

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGroupedByTableGroup() {
        return tableGroupId != null;
    }
}
