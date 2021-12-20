package kitchenpos.table.domain;


import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.exception.CannotUpdatedException;

@Entity
public class OrderTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private GuestNumber numberOfGuests;

    @Embedded
    private EmptyTable empty;

    // TODO: 2021/12/20 orders와 양방향 매핑 

    public OrderTable() {
    }

    private OrderTable(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = GuestNumber.valueOf(numberOfGuests);
        this.empty = EmptyTable.valueOf(empty);
    }

    public static OrderTable of(Integer numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void setTableGroup(TableGroup tableGroup) {
        if (Objects.nonNull(this.tableGroup)) {
            this.tableGroup.removeOrderTable(this);
        }
        this.tableGroup = tableGroup;
        tableGroup.addOrderTable(this);
    }

    public void removeTableGroup() {
        this.tableGroup = null;
    }

    public void updateEmpty(Boolean empty) {
        validateUpdateEmpty();
        this.empty = EmptyTable.valueOf(empty);
    }

    public void updateNumberOfGuests(Integer numberOfGuests) {
        validateUpdateNumberOfGuests();
        this.numberOfGuests = GuestNumber.valueOf(numberOfGuests);
    }

    public void clearTableGroup() {
        this.empty = EmptyTable.valueOf(Boolean.TRUE);
        removeTableGroup();
    }

    private void validateUpdateEmpty() {
        if (Objects.nonNull(tableGroup)) {
            throw new CannotUpdatedException("단체지정된 테이블은 변경할 수 없습니다.");
        }
        // TODO: 2021/12/20 orders와 양방향 매핑후 주문이 진행중인(조리,식사) 테이블은 상태를 변경할 수 없다.
        // validation check
//        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
//            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
//            throw new IllegalArgumentException();
//        }

    }
    
    private void validateUpdateNumberOfGuests() {
        if (empty.isEmpty()) {
            throw new CannotUpdatedException("빈 테이블의 손님수는 변경 할 수 없습니다.");
        }
    }

    public void setEmpty(Boolean empty) {
        this.empty = EmptyTable.valueOf(empty);
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = GuestNumber.valueOf(numberOfGuests);
    }

    public boolean equalTableGroup(TableGroup tableGroup) {
        if (Objects.isNull(this.tableGroup)) {
            return false;
        }
        return this.tableGroup.equals(tableGroup);
    }

    public boolean isNotEmptyTableGroup() {
        return Objects.nonNull(this.tableGroup);
    }

    public boolean isNotEmpty() {
        return empty.equals(Boolean.FALSE);
    }




    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }

    public void setTableGroupId(final Long tableGroupId) {
//        this.tableGroupId = tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.get();
    }

    public void setNumberOfGuestsOrg(final int numberOfGuests) {
//        this.numberOfGuests = numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty.isEmpty();
    }



    public void setEmptyOrg(final boolean empty) {
//        this.empty = empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderTable{" +
            "id=" + id +
            ", tableGroup=" + tableGroup +
            ", numberOfGuests=" + numberOfGuests +
            ", empty=" + empty +
            '}';
    }
}
