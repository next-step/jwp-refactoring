package kitchenpos.order.domain;


import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private Long menuId;

    @Embedded
    private OrderQuantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = OrderQuantity.of(quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public OrderQuantity getQuantity() {
        return quantity;
    }
}
