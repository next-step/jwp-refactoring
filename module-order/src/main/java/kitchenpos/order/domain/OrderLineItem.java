package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    
    @ManyToOne
    private Order order;
    
    private Long menuId;
    
    private long quantity;
    
    protected OrderLineItem() {
    }
    
    private OrderLineItem(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }
    
    public static OrderLineItem of(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
