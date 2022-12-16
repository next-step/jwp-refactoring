package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.exception.InvalidParameterException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    private static final String ERROR_MESSAGE_NOT_EMPTY_TABLE = "모두 비어있는 테이블이어야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private OrderTables orderTables;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    protected TableGroup() {}

    private TableGroup(Long id, List<OrderTable> orderTables) {
        OrderTables tables = OrderTables.from(orderTables);
        validate(tables);
        this.id = id;
        this.orderTables = tables;
    }

    private void validate(OrderTables tables) {
        tables.validAlreadyGroup();
        if (tables.validNotEmptyOrderTable()) {
            throw new InvalidParameterException(ERROR_MESSAGE_NOT_EMPTY_TABLE);
        }
    }

    private TableGroup(List<OrderTable> orderTables) {
        this(null, orderTables);
    }

    public static TableGroup of(long id, List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public Long id() {
        return id;
    }

    public LocalDateTime createdDate() {
        return createdDate;
    }

    public OrderTables orderTables() {
        return orderTables;
    }
}
