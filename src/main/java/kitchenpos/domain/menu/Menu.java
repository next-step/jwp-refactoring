package kitchenpos.domain.menu;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Products;
import kitchenpos.exception.MenuCheapException;
import kitchenpos.exception.ProductNotExistException;

import javax.persistence.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Name name;
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    public static Menu create(MenuCreate create, MenuGroup menuGroup, Products products) throws RuntimeException {
        return create(null, create, menuGroup, products);
    }

    public static Menu create(Long id, MenuCreate create, MenuGroup menuGroup, Products products) throws RuntimeException {
        if (create.getMenuProducts().size() != products.size()) {
            throw new ProductNotExistException();
        }
        Menu menu = new Menu(id, create.getName(), create.getPrice(), menuGroup);

        MenuProducts menuProducts = MenuProducts.create(create.getMenuProducts(), menu, products);

        if (menu.getPrice().compareTo(menuProducts.sumAmount()) > 0) {
            throw new MenuCheapException();
        }

        return menu;
    }

    protected Menu() {
    }

    private Menu(Long id, Name name, Price price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
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
}
