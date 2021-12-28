package kitchenpos.table.domain;

import kitchenpos.domain.NumberOfGuests;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.order.domain.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    private static final String INVALID_GROUP = "그룹테이블에 속해있는 테이블을 빈테이블로 변경할 수 없습니다.";
    private static final String INVALID_EMPTY = "빈 테이블의 손님 수는 변경할 수 없습니다.";
    private static final String INVALID_COMPLETE = "계산 완료인 테이블이 아닌 경우 빈 테이블로 변경할 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {

    }

    private OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable of(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void changeEmpty(boolean empty) {
        validateEmpty();
        this.empty = empty;
    }

    private void validateEmpty() {
        if (!Objects.isNull(tableGroup)) {
            throw new InvalidOrderTableException(INVALID_GROUP);
        }

        if (!isCompleteOrders()) {
            throw new InvalidOrderTableException(INVALID_COMPLETE);
        }
    }

    private void validateNumberOfGuests() {
        if (empty) {
            throw new InvalidOrderTableException(INVALID_EMPTY);
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests();
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public boolean isCompleteOrders() {
        return orders.stream()
                .allMatch(Order::isComplete);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}

