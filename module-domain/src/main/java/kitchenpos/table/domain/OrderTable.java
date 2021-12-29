package kitchenpos.table.domain;


import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;


    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable from(int numberOfGuests, boolean isEmpty) {
        return new OrderTable(numberOfGuests, isEmpty);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void changeEmpty(boolean empty) {
        checkGroupedOrderTable();
        this.empty = empty;
    }

    public void changeNonEmptyOrderTable() {
        this.empty = false;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        checkNumberOfGuestsOverZero();
    }

    public void addTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void checkAvailable() {
        if (!this.isEmpty() || Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("주문테이블이 비어있지 않거나, 단체지정이 되어있습니다.");
        }
    }

    public void checkIsEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }

    public void checkNumberOfGuestsOverZero() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("주문 테이블 손님 수는 0 명 이상으로 입력해야 합니다.");
        }
    }

    public void checkGroupedOrderTable() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("이미 단체지정으로 등록되어 있는 주문 테이블입니다.");
        }
    }

    public void unGroup() {
        this.tableGroupId = null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
