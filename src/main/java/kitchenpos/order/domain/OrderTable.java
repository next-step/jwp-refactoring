package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    @Embedded
    private NumberOfGuest numberOfGuests;
    private boolean empty;
    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty,
        List<Order> orders) {
        this.id = id;
        this.numberOfGuests = new NumberOfGuest(numberOfGuests);
        this.empty = empty;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        if (onCookingOrMeal()) {
            throw new IllegalArgumentException("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");
        }
        this.tableGroupId = null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.numberOfGuest();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void occupied() {
        this.empty = false;
    }

    public void changeEmpty(final boolean empty) {
        validateChangeEmpty();
        this.empty = empty;
    }

    private void validateChangeEmpty() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체 지정된 테이블은 비울 수 없습니다.");
        }

        orders.stream()
            .filter(Order::onCookingOrMeal)
            .findFirst()
            .ifPresent(order -> {
                throw new IllegalArgumentException("조리중 이거나 식사중에는 테이블을 비울 수 없습니다.");
            });
    }

    public boolean onCookingOrMeal() {
        return orders.stream()
            .anyMatch(Order::onCookingOrMeal);
    }

    public void changeNumberOfGuest(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블의 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = new NumberOfGuest(numberOfGuests);
    }

    public boolean ableToGroup() {
        return empty && tableGroupId == null;
    }
}
