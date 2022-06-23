package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer numberOfGuests;
    @Column(nullable = false)
    private Boolean empty;
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Embedded
    private Orders orders = new Orders();

    protected OrderTable() {
    }

    public OrderTable(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void addOrder(Order order) {
        orders.addOrder(order);
    }

    public void checkPossibleChangeEmpty() {
        if (tableGroup != null) {
            throw new IllegalArgumentException("[ERROR] 단체 지정이 되어있어 업데이트 할 수 없습니다.");
        }
        orders.checkPossibleChangeEmpty();
    }

    public void updateEmpty(boolean empty) {
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }
}
