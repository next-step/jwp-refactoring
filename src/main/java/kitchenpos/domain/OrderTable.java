package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;

    @Embedded
    private NumberOfGuest numberOfGuests;

    private boolean empty;

    protected OrderTable() {

    }


    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuest.of(numberOfGuests);
        this.empty = empty;
    }


    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.tableGroupId = tableGroupId;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this(tableGroupId, numberOfGuests, empty);
        this.id = id;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable createOrderTable() {
        return new OrderTable();
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈테이블일 경우 방문자를 변경할 수 없습니다.");
        }
        this.numberOfGuests = NumberOfGuest.of(numberOfGuests);
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = NumberOfGuest.of(numberOfGuests);
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeEmptyTable() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalStateException("테이블 그룹이 있어 변경할 수 없습니다.");
        }
        this.empty = true;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isOrderAble() {
        return empty;
    }
}
