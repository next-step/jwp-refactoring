package kitchenpos.order.domain;

import kitchenpos.order.application.exception.InvalidOrderState;
import kitchenpos.order.application.exception.InvalidTableState;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = {PERSIST, MERGE}, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        orderTables.forEach(this::validate);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables.addAll(orderTables);
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    private void validate(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidTableState("빈 테이블을 일행으로 지정할 수 없습니다.");
        }
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new InvalidTableState("테이블에 일행이 있습니다.");
        }
    }

    public void validateStatus() {
        boolean isAllCompleted = orderTables.stream()
                .allMatch(OrderTable::isCompleted);
        if (!isAllCompleted) {
            throw new InvalidOrderState("모든 주문 상태가 완료되지 않아 단체석을 해제할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
