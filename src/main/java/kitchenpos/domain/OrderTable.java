package kitchenpos.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;

    @Column(name = "empty")
    private boolean isEmpty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    private OrderTable(TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public static OrderTable ofByTableGroupNull(int numberOfGuests, boolean isEmpty) {
        return new OrderTable(null, numberOfGuests, isEmpty);
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void changeEmpty(boolean isEmpty, List<Order> order) {
        validCheckIsNotNullTableGroup();
        validCheckIsOrderStatusInCookingAndMeal(order);

        this.isEmpty = isEmpty;
    }

    private void validCheckIsOrderStatusInCookingAndMeal(List<Order> order) {
        order.forEach(Order::validCheckOrderStatusIsCookingAndMeal);
    }

    private void validCheckIsNotNullTableGroup() {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isNotNull() {
        return this.tableGroup != null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validCheckIsGuestZero(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    private void validCheckIsGuestZero(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
