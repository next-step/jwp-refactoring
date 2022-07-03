package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderStatus;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {}

    public OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(NumberOfGuests numberOfGuests) {
        this(null, null, numberOfGuests, true);
    }

    public void updateEmpty(boolean empty, OrderStatus orderStatus) {
        isPossibleChangeEmpty(orderStatus);

        this.empty = empty;
    }

    public void updateNumberOfGuests(NumberOfGuests numberOfGuests) {
        isPossibleChangeNumberOfGuests();

        this.numberOfGuests = numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public void joinGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void leaveGroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    private void isPossibleChangeEmpty(OrderStatus orderStatus) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("단체 지정에 포함되어 있어서 빈 자리 여부를 변경할 수 없습니다.");
        }
        if (Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL).contains(orderStatus)) {
            throw new IllegalArgumentException("주문 상태가 요리중 또는 식사중인 상태인 주문 테이블의 빈 자리 여부는 변경할 수 없습니다.");
        }
    }

    private void isPossibleChangeNumberOfGuests() {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 자리일 때 손님 수를 변경할 수 없습니다.");
        }
    }
}
