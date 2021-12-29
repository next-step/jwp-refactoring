package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Price;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20)")
    private Long seq;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(nullable = false, columnDefinition = "bigint(20)")
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, String menuName, Price menuPrice, long quantity) {
        this.menuId = menuId;
        this.name = menuName;
        this.price = menuPrice;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
