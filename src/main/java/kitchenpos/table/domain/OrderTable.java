package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "order_table")
@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private TableGuest numberOfGuests;

    private boolean empty;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "orderTable")
    private Order order;

    protected OrderTable() {

    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new TableGuest(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable setting(int numberOfGuests) {
        return new OrderTable(numberOfGuests, true);
    }

    public static OrderTable setting(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumber();
    }

    public boolean isEmpty() {
        return empty;
    }

    public Order getOrder() {
        return order;
    }

    private boolean isGrouping() {
        return Objects.nonNull(this.tableGroupId);
    }

    public void validateFullAndTableGrouping() {
        if (!isEmpty() || isGrouping()) {
            throw new IllegalArgumentException("비어있지 않거나 단체로 지정되어있습니다.");
        }
    }

    private void validateTableGrouping() {
        if (isGrouping()) {
            throw new IllegalArgumentException("단체로 지정되어있습니다.");
        }
    }

    private void validateChangeNumberOfGuestsByIsEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있을 때 손님 수를 조정할 수 없습니다.");
        }
    }

    private void validateOrderStatusCompletion() {
        if(Objects.nonNull(this.order) && (this.order.isCooking() || this.order.isMeal())) {
            throw new IllegalArgumentException("요리중이거나 식사중입니다.");
        }
    }

    public void empty() {
        validateTableGrouping();
        validateOrderStatusCompletion();
        this.empty = true;
    }

    public void unGrouping() {
        validateOrderStatusCompletion();
        this.tableGroupId = null;
    }

    public void full() {
        validateTableGrouping();
        validateOrderStatusCompletion();
        this.empty = false;
    }

    public Order placeOrder() {
        if (isEmpty()) {
            throw new IllegalArgumentException("비어있는 주문 테이블에서는 주문할 수 없습니다.");
        }
        this.order = Order.create(this);
        return this.order;
    }

    public void grouping(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateChangeNumberOfGuestsByIsEmpty();
        this.numberOfGuests = new TableGuest(numberOfGuests);
    }

}
