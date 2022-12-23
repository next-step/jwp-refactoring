package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUEST = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;
    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    public OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this(id, tableGroupId, numberOfGuests, empty, Collections.emptyList());
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty, List<Order> orders) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        validateChangeEmpty();
        this.empty = empty;
    }

    public boolean onCookingOrMeal() {
        for (Order order : orders) {
            if (order.onCookingOrMeal()) {
                return true;
            }
        }
        return false;
    }

    private void validateChangeEmpty() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }

        orders.forEach(
            order -> {
                if (order.onCookingOrMeal()) {
                    throw new IllegalArgumentException("조리중 이거나 식사중에는 테이블을 비울 수 없습니다.");
                }
            }
        );
    }

    public void changeNumberOfGuest(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUEST) {
            throw new IllegalArgumentException("손님의 수는 0명 이하일 수 없습니다.");
        }

        if (empty) {
            throw new IllegalArgumentException("빈 테이블의 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }
}
