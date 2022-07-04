package kitchenpos.table.domain;

import static kitchenpos.table.domain.OrderTables.MIN_ORDERTABLES_SIZE;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTableList) {
        this(null, null, orderTableList);
    }

    public TableGroup(Long id, List<OrderTable> orderTableList) {
        this(id, null, orderTableList);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTableList) {
        validate(orderTableList);
        this.id = id;
        this.createdDate = createdDate;
        orderTableList.forEach(orderTable -> orderTables.add(orderTable));
    }

    private void validate(List<OrderTable> orderTableList) {
        validateOrderTableSize(orderTableList);
        validateOrderTableEmpty(orderTableList);
    }

    private void validateOrderTableSize(List<OrderTable> orderTableList) {
        if (CollectionUtils.isEmpty(orderTableList) || orderTableList.size() < MIN_ORDERTABLES_SIZE) {
            throw new IllegalArgumentException("테이블이 2개 이상이어야 합니다.");
        }
    }

    private void validateOrderTableEmpty(List<OrderTable> orderTableList) {
        orderTableList.stream()
                .filter(orderTable -> !orderTable.isEmpty())
                .findFirst()
                .ifPresent(orderTable -> {
                    throw new IllegalArgumentException("비어있지 않은 테이블이 존재합니다.");
                });
    }

    public void ungroup() {
        orderTables.clear();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
