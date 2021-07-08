package kitchenpos.domain.order;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    private Long orderId;

    private Long menuId;

    @AttributeOverride(name = "name", column = @Column(name = "menu_name"))
    private Name menuName;

    @AttributeOverride(name = "price", column = @Column(name = "menu_price"))
    private Price menuPrice;

    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long orderId, Long menuId, Name menuName, Price menuPrice, Quantity quantity) {
        this(null, orderId, menuId, menuName, menuPrice, quantity);
    }

    public OrderLineItem(Long id, Long orderId, Long menuId, Name menuName, Price menuPrice, Quantity quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Name getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }

    public Long getId() {
        return id;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
