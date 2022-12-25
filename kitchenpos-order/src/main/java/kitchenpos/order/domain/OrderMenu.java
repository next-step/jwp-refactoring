package kitchenpos.order.domain;


import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class OrderMenu {

    @Column(name = "menu_id")
    private Long menuId;

    @AttributeOverride(name = "name", column = @Column(name = "menu_name", nullable = false))
    private Name menuName;

    @AttributeOverride(name = "price", column = @Column(name = "menu_price", nullable = false))
    private Price menuPrice;

    protected OrderMenu() {}

    public OrderMenu(Long menuId, String menuName, BigDecimal menuPrice) {
        this.menuId = menuId;
        this.menuName = Name.from(menuName);
        this.menuPrice = Price.from(menuPrice);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Name getMenuName() {
        return menuName;
    }

    public String getMenuNameValue() {
        return menuName.value();
    }

    public Price getMenuPrice() {
        return menuPrice;
    }

    public BigDecimal getMenuPriceValue() {
        return menuPrice.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderMenu)) {
            return false;
        }
        OrderMenu orderMenu = (OrderMenu) o;
        return Objects.equals(menuId, orderMenu.menuId) && Objects.equals(menuName, orderMenu.menuName) && Objects.equals(menuPrice,
                orderMenu.menuPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, menuName, menuPrice);
    }

    public static class Builder {

        private Long menuId;
        private String menuName;
        private BigDecimal menuPrice;

        public Builder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder menuName(String menuName) {
            this.menuName = menuName;
            return this;
        }

        public Builder menuPrice(BigDecimal menuPrice) {
            this.menuPrice = menuPrice;
            return this;
        }

        public OrderMenu build() {
            return new OrderMenu(menuId, menuName, menuPrice);
        }
    }
}
