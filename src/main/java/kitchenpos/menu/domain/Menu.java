package kitchenpos.menu.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.exception.MenuPriceEmptyException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price = Price.wonOf(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(Long id) {
        this.id = id;
    }

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, new MenuGroup(menuGroupId), menuProducts);
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validationPrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProducts, this);
    }

    private void validationPrice(Price price) {
        if (Objects.isNull(price)) {
            throw new MenuPriceEmptyException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

}
