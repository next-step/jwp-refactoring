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

    public Menu(Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        menuProducts.setMenu(this);
    }

    public Menu(Long id, Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this(name, price, menuGroupId, menuProducts);
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
