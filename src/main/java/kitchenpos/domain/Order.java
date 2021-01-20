package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;
    @CreatedDate
    private LocalDateTime createdTime;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderMenu> orderMenus = new ArrayList<>();

    protected Order(){}

    public Order(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public List<OrderMenu> getOrderMenus() {
        return orderMenus;
    }

    public void add(Menu menu, Long quantity) {
        this.orderMenus.add(new OrderMenu(this, menu, quantity));
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public boolean isComplete() {
        return orderStatus.equals(OrderStatus.COMPLETION);
    }

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
