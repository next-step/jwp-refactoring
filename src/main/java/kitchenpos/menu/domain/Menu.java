package kitchenpos.menu.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.exception.Message;
import kitchenpos.product.domain.Amount;
import org.apache.logging.log4j.util.Strings;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private Amount price;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public static Menu of(Long id, String name, Amount price, MenuGroup menuGroup) {
        return new Menu(id, name, menuGroup, price);
    }

    public static Menu of(String name, Amount price, MenuGroup menuGroup) {
        return new Menu(null, name, menuGroup, price);
    }

    private Menu(Long id, String name, MenuGroup menuGroup, Amount price) {

        if (Strings.isBlank(name)) {
            throw new IllegalArgumentException(Message.MENU_NAME_IS_NOT_NULL.getMessage());
        }

        this.id = id;
        this.name = name;
        this.menuGroup = menuGroup;
        this.price = price;
    }

    protected Menu() {
    }

    public void withMenuProducts(List<MenuProduct> menuProducts) {
        Amount sumAmount = MenuProducts.of(menuProducts)
                                       .sum();
        if (price.grateThan(sumAmount)) {
            throw new IllegalArgumentException(Message.MENU_AMOUNT_IS_TOO_LAGE.getMessage());
        }
        this.menuProducts.addAll(menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public Amount getPrice() {
        return price;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
    public List<MenuProduct> getProducts(){
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
