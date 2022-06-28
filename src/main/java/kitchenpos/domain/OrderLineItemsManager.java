package kitchenpos.domain;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.ObjectUtils;

@Embeddable
public class OrderLineItemsManager {

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new LinkedList<>();

    protected OrderLineItemsManager() {
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void remove(OrderLineItem orderLineItem){
        orderLineItems.remove(orderLineItem);
    }

    public void add(OrderLineItem orderLineItem){
        if(!ObjectUtils.isEmpty(orderLineItem.getOrder())){
            orderLineItem.getOrder().removeOrderLineItem(orderLineItem);
        }

        orderLineItems.add(orderLineItem);
    }
}
