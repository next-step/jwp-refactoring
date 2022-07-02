package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.Orders;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private NumberOfGuest numberOfGuests;
    @Column(nullable = false)
    private TableEmpty empty;

    protected OrderTable() {
    }

    public OrderTable(NumberOfGuest numberOfGuests, TableEmpty empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(Long id, NumberOfGuest numberOfGuests, TableEmpty empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeNumberOfGuests(NumberOfGuest numberOfGuests) {
        validateChangeNumberOfGuests();
        this.numberOfGuests = numberOfGuests;
    }

    public void changeOrderTable() {
        empty = new TableEmpty(false);
    }

    public boolean isEmptyTable() {
        return empty.isEmpty();
    }

    public boolean isOrderTable() {
        return !empty.isEmpty();
    }

    public void changeEmpty(TableEmpty empty, Orders orders) {
        validateChangeEmpty(orders);
        this.empty = empty;
    }

    private void validateChangeNumberOfGuests() {
        if (empty.isEmptyTable()) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블은 방문 손님 수를 변경할 수 없습니다.");
        }
    }

    private void validateChangeEmpty(Orders orders) {
        if (orders.containOrderStatus(OrderStatus.COOKING)) {
            throw new IllegalArgumentException("[ERROR] 조리 상태인 주문이 있어 빈 테이블 여부 업데이트 할 수 없습니다. ");
        }
        if (orders.containOrderStatus(OrderStatus.MEAL)) {
            throw new IllegalArgumentException("[ERROR] 식사 상태인 주문이 있어 빈 테이블 여부 업데이트 할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public NumberOfGuest getNumberOfGuests() {
        return numberOfGuests;
    }

    public TableEmpty getEmpty() {
        return empty;
    }

}
