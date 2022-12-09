package kitchenpos.menu.domain;

import io.micrometer.core.instrument.util.StringUtils;
import kitchenpos.menu.exception.MenuExceptionCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false, unique = true)
    private String name;

    @Embedded
    private MenuPrice price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        validate(name, menuGroup);

        this.name = name;
        this.price = new MenuPrice(price);
        this.menuGroup = menuGroup;
    }

    private void validate(String name, MenuGroup menuGroup) {
        if(StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException(MenuExceptionCode.REQUIRED_NAME.getMessage());
        }

        if(Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException(MenuExceptionCode.REQUIRED_MENU_GROUP.getMessage());
        }
    }

    public void create(List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct -> addMenuProduct(menuProduct));
        validatePrice();
    }

    void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.addMenuProduct(this, menuProduct);
    }

    private void validatePrice() {
        this.menuProducts.validatePrice(this.price.getPrice());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
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
        return Objects.equals(name, menu.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
