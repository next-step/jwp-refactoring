package kitchenpos.order.domain;

import kitchenpos.exception.BadRequestException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.exception.AlreadyCompleteException;
import kitchenpos.menu.domain.Menu;
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
        if (orderTable.isEmpty()) {
            throw new BadRequestException("빈 테이블은 주문을 받을 수 없습니다");
        }
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

    public void changeStatus(OrderStatus orderStatus) {
        if (isComplete()) {
            throw new AlreadyCompleteException("이미 완료된 주문입니다.");
        }
        this.orderStatus = orderStatus;
    }

    private boolean isComplete() {
        return orderStatus.equals(OrderStatus.COMPLETION);
    }
}
