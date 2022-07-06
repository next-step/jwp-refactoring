package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.dto.MenuRequest;
import org.springframework.util.ObjectUtils;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;
    @Embedded
    private Price price;

    protected Menu() {

    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(name, price, menuGroup, menuProducts);
        this.id = id;
    }

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validate(name, price, menuGroup, menuProducts);
        validatePrice(price, menuProducts.totalAmount());
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        this.menuProducts.changeMenu(this);
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(name, Price.from(price), menuGroup, menuProducts);

    }

    public static Menu createMenu(MenuRequest menuRequest, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);
    }

    private void validatePrice(Price price, Amount totalAmount) {
        if (price.isBigThen(totalAmount)) {
            throw new IllegalArgumentException("메뉴의 가격은 구성하고 있는 메뉴 상품들의 가격(상품가격 * 수량)의 합계보다 작아야 합니다.");
        }
    }

    private void validate(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validateEmpty(name, "이름은 필수 이어야 합니다.");
        validateEmpty(price, "가격은 필수 이어야 합니다.");
        validateEmpty(menuGroup, "메뉴그룹은 필수 이어야 합니다.");
        validateEmpty(menuProducts, "메뉴상품들은 필수 이어야 합니다.");
    }

    private void validateEmpty(Object obj, String msg) {
        if (ObjectUtils.isEmpty(obj)) {
            throw new IllegalArgumentException(msg);
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
        return menuProducts.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(getName(), menu.getName()) && Objects.equals(getPrice(), menu.getPrice())
                && Objects.equals(getMenuGroup(), menu.getMenuGroup()) && Objects.equals(
                getMenuProducts(), menu.getMenuProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getMenuGroup(), getMenuProducts());
    }
}
