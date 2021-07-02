package kitchenpos.domain;

import static java.util.Objects.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {}

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.valueOf(numberOfGuests);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void ungrouped() {
        this.tableGroup = null;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final NumberOfGuests numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 방문 손님 수를 수정할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmpty() {
        return !empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    void groupBy(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean isGrouped() {
        return nonNull(tableGroup);
    }

    public void emptyOn() {
        validateNotGrouped();
        this.empty = true;
    }

    public void emptyOff() {
        validateNotGrouped();
        this.empty = false;
    }

    private void validateNotGrouped() {
        if (isGrouped()) {
            throw new IllegalArgumentException("그룹 설정이 되어 있는 테이블은 주문 등록 불가 상태로 바꿀 수 없습니다.");
        }
    }
}
