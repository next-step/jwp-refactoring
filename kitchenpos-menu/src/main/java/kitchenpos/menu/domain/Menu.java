package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Menu {
    private static final String REQUIRED_MENU_GROUP = "메뉴 그룹은 필수 값 입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuName name;

    @Embedded
    private MenuPrice price;

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        validateMenuGroup(menuGroupId);
        this.name = new MenuName(name);
        this.price = new MenuPrice(price);
        this.menuGroupId = menuGroupId;
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (Objects.isNull(menuGroupId)) {
            throw new IllegalArgumentException(REQUIRED_MENU_GROUP);
        }
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.addMenu(this);
    }

    public boolean moreExpensive(MenuPrice price) {
        return this.price.greaterThan(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.value();
    }
}
