package kitchenpos.menu.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(Name name, Price price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(Long id, Name name, Price price, Long menuGroupId) {
        this(name, price, menuGroupId);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.get();
    }

    public void setMenuProducts(MenuProducts menuProducts) {
        validatePrice(menuProducts.totalMenuPrice());

        this.menuProducts = menuProducts;
        menuProducts.setMenu(this);
    }

    private void validatePrice(Price totalPrice) {
        if (price.isBiggerThan(totalPrice)) {
            throw new IllegalArgumentException(ErrorCode.MENU_PRICE_SHOULD_NOT_OVER_TOTAL_PRICE.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
