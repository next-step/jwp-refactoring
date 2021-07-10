package kitchenpos.domain.order;

import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "table_group")
@Entity
public class OrderTableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    @ReadOnlyProperty
    private List<OrderTable> orderTables;

    protected OrderTableGroup() {
    }

    private OrderTableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static OrderTableGroup of(Long id, LocalDateTime createdDate) {
        return new OrderTableGroup(id, createdDate, new ArrayList<>());
    }

    public static OrderTableGroup of(LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new OrderTableGroup(null, createdDate, orderTables);
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
