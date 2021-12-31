package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    private boolean empty;

    @OneToMany
	@JoinColumn(name = "orderTableId")
	private List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = new ArrayList<>();
    }

    private OrderTable(int numberOfGuests, boolean empty, List<Order> orders) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 손님 수는 0명 이상이어야 합니다.");
        }
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable ofOrder(int numberOfGuests, boolean empty, List<Order> orders) {
        return new OrderTable(numberOfGuests, empty, orders);
    }

    public void changeEmpty(boolean empty) {
        if (isGrouped()) {
            throw new IllegalArgumentException("단체 지정 된 테이블은 상태를 변경할 수 없습니다.");
        }

        validateOrderStatus();
        this.empty = empty;
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        grouped();
    }

    public void ungroup() {
        validateOrderStatus();
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroupId);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 빈 테이블이면 방문자 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    private void grouped() {
        this.empty = false;
    }

    private void validateOrderStatus() {
        boolean isNotCooking = orders
                .stream()
                .anyMatch(Order::isNotCooking);
        if (isNotCooking) {
            throw new IllegalArgumentException("주문 테이블의 상태가 조리이거나, 식사이면 변경할 수 없습니다.");
        }
    }
}
