package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.util.ObjectUtils;

@Entity
public class Menu {
    private static final String NAME_NOT_ALLOW_NULL_OR_EMPTY = "메뉴명은 비어있거나 공백일 수 없습니다.";
    private static final String REQUIRED_MENU_GROUP = "메뉴 그룹은 필수 값 입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private MenuPrice price;

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        validateName(name);
        validateMenuGroup(menuGroupId);
        this.name = name;
        this.price = new MenuPrice(price);
        this.menuGroupId = menuGroupId;
    }

    private void validateName(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalArgumentException(NAME_NOT_ALLOW_NULL_OR_EMPTY);
        }
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
        return name;
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
