package kitchenpos.order.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import kitchenpos.generic.price.domain.Price;
import kitchenpos.generic.quantity.domain.Quantity;
import kitchenpos.menu.domain.MenuDetailOption;
import kitchenpos.menu.domain.MenuOption;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String name;

    private Price price;

    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private Long menuId;

    @Embedded
    @Column(nullable = false)
    private Quantity quantity;

    @Embedded
    private OrderLineItemDetails orderLineItemDetails = new OrderLineItemDetails();

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, String name, Price price, Quantity quantity,
            OrderLineItemDetails orderLineItemDetails) {
        this(null, menuId, name, price, quantity, orderLineItemDetails);
    }

    OrderLineItem(Long seq, Long menuId, String name, Price price, Quantity quantity,
            OrderLineItemDetails orderLineItemDetails) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.menuId = menuId;
        this.quantity = quantity;
        this.orderLineItemDetails = orderLineItemDetails;
    }

    public String getName() {
        return name;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Price getPrice() {
        return price;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderLineItem that = (OrderLineItem)o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    public MenuOption getMenuOption() {
        return new MenuOption(this.name, this.price);
    }

    public List<MenuDetailOption> getMenuDetailOptions() {
        return this.orderLineItemDetails.toMenuDetailOptions();
    }
}
