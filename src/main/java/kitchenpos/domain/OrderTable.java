package kitchenpos.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수는 불가능합니다.");
        }

        if (isEmpty()) {
            throw new IllegalArgumentException("해당테이블은 비어있어 수정이 불가능합니다");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("그룹 설정이 되어 있는 주문테이블은 상태를 변경할수 없습니다.");
        }

        if (isCookingAndMealStatus()) {
            String errorMsg = String.format("%s 또는 %s 상태일때는 변경할수 없습니다.",
                    OrderStatus.COOKING.remark(), OrderStatus.MEAL.remark());

            throw new IllegalArgumentException(errorMsg);
        }

        this.empty = empty;
    }

    private boolean isCookingAndMealStatus() {
        return orders.stream()
            .filter(order -> Objects.equals(this, order.getOrderTable()))
            .anyMatch(order -> order.equalsByOrderStatus(OrderStatus.COOKING) ||
                    order.equalsByOrderStatus(OrderStatus.MEAL));
    }

    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void clearTableGroup() {
        if (isCookingAndMealStatus()) {
            throw new IllegalArgumentException(String.format("%s 또는 %s 상태일때는 삭제할수 없습니다.", OrderStatus.COOKING, OrderStatus.MEAL));
        }

        this.tableGroup = null;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
