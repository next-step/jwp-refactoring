package kitchenpos.order.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

/**
 * CREATE TABLE table_group (
 * id BIGINT(20) NOT NULL AUTO_INCREMENT,
 * created_date DATETIME NOT NULL,
 * PRIMARY KEY (id)
 * );
 */


@Entity
@Table(name = "table_group")
@EntityListeners(AuditingEntityListener.class)
public class TableGroup extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long id;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
//        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.values();
    }
}
