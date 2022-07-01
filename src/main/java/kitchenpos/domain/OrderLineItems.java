package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> elements = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> elements) {
        validateOrderLineItems(elements);
        this.elements = new ArrayList<>(elements);
    }

    private void validateOrderLineItems(List<OrderLineItem> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            throw new IllegalArgumentException("주문 항목이 없습니다.");
        }
    }
}
