package order.domain;

import common.domain.Price;
import common.domain.Quantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private Long menuId;
    private String menuName;
    @Embedded
    @AttributeOverride(name = "price", column = @Column(name = "menu_price"))
    private Price menuPrice;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long menuId, long quantity, String menuName, Price menuPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = Quantity.from(quantity);
    }

    public static OrderLineItem of(Long menuId, long quantity, String menuName, Price menuPrice) {
        return new OrderLineItem(menuId, quantity, menuName, menuPrice);
    }

    public Long getSeq() {
        return seq;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity.value();
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getPrice() {
        return menuPrice;
    }
}
