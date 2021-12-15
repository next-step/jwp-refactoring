package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import org.springframework.util.Assert;

@Embeddable
public class OrderLineItemMenu {

    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private long menuId;

    @Embedded
    private Price price;

    @Embedded
    private Name name;

    protected OrderLineItemMenu() {
    }

    private OrderLineItemMenu(long menuId, Name name, Price price) {
        Assert.notNull(price, "가격은 필수입니다.");
        Assert.notNull(name, "이름은 필수입니다.");
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static OrderLineItemMenu of(long menuId, Name name, Price price) {
        return new OrderLineItemMenu(menuId, name, price);
    }

    public long id() {
        return menuId;
    }

    public Price getPrice() {
        return price;
    }

    public Name getName() {
        return name;
    }
}
