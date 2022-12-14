package kitchenpos.domain;

import static kitchenpos.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_TABLES_COUNT_AND_SAVED_ORDER_TABLES;
import static kitchenpos.exception.ErrorCode.ORDER_TABLES_MUST_BE_AT_LEAST_TWO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.exception.KitchenposException;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "tableGroupId")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
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

    public List<Long> getOrderTableIds(){
        return this.orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void validateOrderTables() {
        if (isOrderTablesEmpty() || isLessThanTwo()) {
            throw new KitchenposException(ORDER_TABLES_MUST_BE_AT_LEAST_TWO);
        }
    }

    private boolean isOrderTablesEmpty(){
        return CollectionUtils.isEmpty(this.orderTables);
    }

    private boolean isLessThanTwo(){
        return this.orderTables.size() < 2;
    }

    public void validateOrderTablesSize(int savedOrderTablesSize) {
        if (this.orderTables.size() != savedOrderTablesSize) {
            throw new KitchenposException(NOT_SAME_BETWEEN_ORDER_TABLES_COUNT_AND_SAVED_ORDER_TABLES);
        }
    }
}
