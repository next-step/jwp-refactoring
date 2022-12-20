package kitchenpos.table.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private Empty empty;

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = Empty.from(empty);
    }

    protected OrderTable() {
    }

    public void bindTo(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = Empty.from(false);
    }

    public void unbind() {
        tableGroup = null;
    }

    public boolean isGrouped() {
        return tableGroup != null;
    }

    public boolean isEmpty() {
        return empty.isTrue();
    }

    public void changeEmpty(boolean empty) {
        if (isGrouped()) {
            throw new IllegalStateException("주문 테이블의 상태를 변경할 수 없습니다.");
        }
        this.empty = Empty.from(empty);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalStateException("인원수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }
}
