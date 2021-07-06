package kitchenpos.domain.order;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    private Long menuId;

    @AttributeOverride(name = "name", column = @Column(name = "menu_name"))
    private Name menuName;

    @AttributeOverride(name = "price", column = @Column(name = "menu_price"))
    private Price menuPrice;

    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu, Quantity quantity) {
        this(null, menu, quantity);
    }

    public OrderLineItem(Order order, Menu menu, Quantity quantity) {
        this(null, order, menu, quantity);
    }

    public OrderLineItem(Long id, Order order, Menu menu, Quantity quantity) {
        this.id = id;
        this.order = order;
        this.menuId = menu.getId();
        this.menuName = menu.getName();
        this.menuPrice = menu.getPrice();
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
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

    void changeOrder(Order order) {
        if (this.order != null) {
            throw new IllegalArgumentException();
        }

        this.order = order;
    }
}
