package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private Orders orders = new Orders();
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (Objects.nonNull(this.tableGroup)) {
            return tableGroup.getId();
        }
        return null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void updateTableGroup(TableGroup tableGroup) {
        if(Objects.isNull(tableGroup)) {
            throw new IllegalArgumentException("단체를 NULL 로 지정할 수 없습니다.");
        }

        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void updateEmpty(boolean empty) {
        checkTableGroup();
        orders.checkOrderStatus();
        this.empty = empty;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        checkIsNotEmpty();
        this.numberOfGuests.update(numberOfGuests);
    }

    private void checkTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체지정된 테이블의 공석여부는 변경할 수 없습니다.");
        }
    }

    private void checkIsNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블의 손님 수는 변경할 수 없습니다.");
        }
    }
}
