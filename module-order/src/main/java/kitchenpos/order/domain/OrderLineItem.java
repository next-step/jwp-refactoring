package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;
import kitchenpos.order.dto.OrderLineItemResponse;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem(){
    }

    OrderLineItem(Builder builder) {
        this.seq = builder.seq;
        this.order = builder.order;
        this.menuId = builder.menuId;
        this.quantity = builder.quantity;
    }

    public OrderLineItemResponse toOrderLineItemResponse() {
        return new OrderLineItemResponse(this.seq, this.menuId, this.quantity.value());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq) && Objects.equals(order, that.order)
                && Objects.equals(menuId, that.menuId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, order, menuId, quantity);
    }

    public static class Builder {
        private Long seq;
        private Orders order;
        private Long menuId;
        private Quantity quantity;

        public Builder(Orders order) {
            this.order = order;
        }

        public Builder setSeq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder setOrder(Orders order) {
            this.order = order;
            return this;
        }

        public Builder setMenuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder setQuantity(Quantity quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem builder() {
            return new OrderLineItem(this);
        }
    }
}
