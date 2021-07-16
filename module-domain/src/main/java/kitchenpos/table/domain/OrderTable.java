package kitchenpos.table.domain;

import kitchenpos.common.Exception.AlreadyGroupedException;
import kitchenpos.common.Exception.IsNotEmptyException;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean isEmpty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = isEmpty;
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }


    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty) {
        isTableGroupEmptyCheck();
        this.empty = empty;
    }

    public void updateTableGroup(Long tableGroupId) {
        updatePossibleCheck();
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    private void updatePossibleCheck() {
        availableToUpdateCheck();
        isTableGroupEmptyCheck();
    }

    private void isTableGroupEmptyCheck() {
        if (Objects.nonNull(tableGroupId)) {
            throw new AlreadyGroupedException("단체 지정된 주문 테이블입니다.");
        }
    }

    protected void availableToUpdateCheck() {
        if (!empty) {
            throw new IsNotEmptyException("빈 주문 테이블이 아닙니다.");
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }


    public void releaseGroup() {
        this.tableGroupId = null;
    }

}
