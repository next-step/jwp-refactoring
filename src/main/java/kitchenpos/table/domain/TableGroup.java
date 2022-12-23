package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "tableGroupId")
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate,
        List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            if(orderTable.onCookingOrMeal()){
                throw new IllegalArgumentException("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");
            }
            orderTable.setTableGroupId(null);
        }
    }
}
