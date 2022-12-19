package kitchenpos.menu.domain;

import static javax.persistence.GenerationType.*;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;

@Entity
public class Menu {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(Price price, Name name, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validatePrice(price, menuProducts);

        for (MenuProduct menuProduct : menuProducts) {
            this.menuProducts.add(new MenuProduct(this, menuProduct.getProduct(), menuProduct.getQuantity()));
        }
        this.price = price;
        this.name = name;
        this.menuGroup = menuGroup;
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    private void validatePrice(Price price, List<MenuProduct> menuProducts) {
        Price sumPriceOfProducts = menuProducts.stream()
            .map(MenuProduct::sumOfPrice)
            .reduce(Price::add)
            .orElseThrow(RuntimeException::new);
        if (sumPriceOfProducts.lessThan(price)) {
            throw new InvalidMenuPriceException();
        }
    }

    public List<MenuProductResponse> getMenuProductResponse() {
        return this.menuProducts.getMenuProductResponse();
    }
}
