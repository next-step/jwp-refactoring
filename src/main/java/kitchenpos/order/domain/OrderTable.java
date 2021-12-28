package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();


    private boolean empty;

    protected OrderTable() {

    }

    public OrderTable(int numberOfGuests, boolean empty, List<Order> orders) {
        this(numberOfGuests, empty);
        this.orders = orders;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public void seatNumberOfGuests(int numberOfGuests) {
        checkTableEmpty();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    private void checkTableEmpty() {
        if(this.isEmpty()){
           throw new InputTableDataException(InputTableDataErrorCode.THE_STATUS_IS_ALEADY_EMPTY);
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void cancelTableGroup() {
        if (isCookingOrEatingOrder()) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_IS_COOKING_OR_IS_EATING);
        }
        this.tableGroup = null;
    }

    public void allocateTableGroup(TableGroup tableGroup){
        this.tableGroup = tableGroup;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void enterGuest() {
        this.empty = false;
    }

    public void leaveGuest() {
        this.empty = true;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public List<Order> getOrders() {
        return orders;
    }

    private boolean isCookingOrEatingOrder() {
        return this.orders.stream().anyMatch(it -> it.isCooking() || it.isEating());
    }

    public void updateEmpty(boolean empty) {
        checkUpdateTableGroup();
        if (empty) {
            this.leaveGuest();
            return;
        }
        this.enterGuest();
    }

    private void checkUpdateTableGroup() {
        if (this.tableGroup != null) {
            throw new InputTableDataException(InputTableDataErrorCode.THE_TABLE_HAS_GROUP);
        }

        if (this.isCookingOrEatingOrder()) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_IS_COOKING_OR_IS_EATING);
        }
    }
}
