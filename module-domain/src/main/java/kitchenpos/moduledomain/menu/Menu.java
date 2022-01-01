package kitchenpos.moduledomain.menu;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.moduledomain.common.exception.DomainMessage;
import kitchenpos.moduledomain.product.Amount;
import org.apache.logging.log4j.util.Strings;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private Long menuGroupId;

    @Embedded
    private Amount price;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public static Menu of(Long id, String name, Amount price, Long menuGroupId) {
        return new Menu(id, name, menuGroupId, price);
    }

    public static Menu of(String name, Amount price, Long menuGroupId) {
        return new Menu(null, name, menuGroupId, price);
    }

    private Menu(Long id, String name, Long menuGroupId, Amount price) {
        validIsNotNull(name);
        this.id = id;
        this.name = name;
        this.menuGroupId = menuGroupId;
        this.price = price;
    }

    protected Menu() {
    }

    private void validIsNotNull(String name) {
        if (Strings.isBlank(name)) {
            throw new IllegalArgumentException(DomainMessage.MENU_NAME_IS_NOT_NULL.getMessage());
        }
    }

    public void addMenuProducts(MenuProducts menuProducts, MenuValidation menuValidation) {
        this.menuProducts = menuProducts;
        menuValidation.validSumProductPrice(price, menuProducts);
        menuValidation.validExistsMenuGroup(menuGroupId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public Amount getPrice() {
        return price;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public List<MenuProduct> getProducts() {
        return menuProducts.getMenuProducts();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
