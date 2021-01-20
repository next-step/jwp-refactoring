package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.Objects;

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

    @Column(name = "empty")
    private boolean empty;

    @Embedded
    private Orders orders = new Orders();;

    protected OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void updateTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void updateEmpty(boolean empty) {
        checkOrderTableGroup();
        checkOrderTableStatus();
        this.empty = empty;
    }

    public void unGroup() {
        if (orders.hasNotComplete()) {
            throw new IllegalArgumentException("주문이 완료되지 않은 테이블은 삭제할 수 없습니다.");
        }
        this.tableGroup = null;
    }

    private void checkOrderTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("그룹 지정이 되어 있어 상태를 변경할 수 없습니다.");
        }
    }

    private void checkOrderTableStatus() {
        if (isNotComplete()) {
            throw new IllegalArgumentException("주문 상태가 조리중 또는 식사중인 테이블의 상태는 변경할 수 없습니다.");
        }
    }

    public boolean isNotComplete() {
        return orders.hasNotComplete();
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        checkOrderTableNotEmpty();
        this.numberOfGuests.update(numberOfGuests);
    }

    private void checkOrderTableNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블의 손님 수는 변경할 수 없습니다.");
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if(Objects.nonNull(tableGroup)){
            return tableGroup.getId();
        }
        return null;
    }

    public void isOrderTableEmptyOrAssigned() {
        if (!empty || Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("비어있지 않거나 이미 그룹 지정된 테이블은 그룹 지정할 수 없습니다.");
        }
    }
}
