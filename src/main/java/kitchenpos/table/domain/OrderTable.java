package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    private static final int ZERO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberOfGuests;
    private boolean empty;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void groupTable(TableGroup tableGroup) {
        validateTableGroupCheck();
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroupTable() {
        this.tableGroup = null;
        this.empty = true;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateEmptyTableCheck();
        validateGuestNumberCheck(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    private void validateTableGroupCheck() {
        if (!isEmpty() || Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("주문 진행중이거나 이미 단체 테이블인 경우 단체로 지정할 수 없습니다.");
        }
    }

    private void validateEmptyTableCheck() {
        if (isEmpty()) {
            throw new IllegalArgumentException("변경하려는 테이블은 빈 테이블이어야 합니다.");
        }
    }
    private void validateGuestNumberCheck(int numberOfGuests) {
        if (numberOfGuests < ZERO) {
            throw new IllegalArgumentException("변경하려는 사용자의 수는 0명 이상이어야 합니다.");
        }
    }
}
