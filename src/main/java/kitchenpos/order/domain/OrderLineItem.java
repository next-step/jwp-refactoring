package kitchenpos.order.domain;


import java.math.BigDecimal;
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
import javax.persistence.Table;
import kitchenpos.global.domain.Name;
import kitchenpos.global.domain.Price;
import kitchenpos.global.domain.Quantity;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long menuId;

    @Column(name = "menu_price")
    @Embedded
    private Price price;
    @Embedded
    @Column(name = "menu_name")
    private Name name;


    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {

    }

    private OrderLineItem(Long menuId, Price menuPrice, Name menuName, Quantity quantity) {
        validateMenu(menuId);
        this.quantity = quantity;
        this.price = menuPrice;
        this.name = menuName;
        this.menuId = menuId;

    }

    private OrderLineItem(Long menuId, BigDecimal menuPrice, String menuName, long quantity) {
        this(menuId, Price.from(menuPrice), new Name(menuName), Quantity.from(quantity));
    }

    public static OrderLineItem of(Long menuId, BigDecimal menuPrice, String menuName, long quantity) {
        return new OrderLineItem(menuId, menuPrice, menuName, quantity);
    }

    private void validateMenu(Long menuId) {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException("메뉴가 없습니다.");
        }
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity.value();
    }

    public Long getSeq() {
        return seq;
    }

    public void changeOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
