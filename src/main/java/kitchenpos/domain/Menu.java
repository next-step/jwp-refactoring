package kitchenpos.domain;

import kitchenpos.exception.MenuCheapException;
import kitchenpos.exception.ProductNotExistException;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    private MenuProducts menuProducts = new MenuProducts();


    public static Menu create(MenuCreate create, MenuGroup menuGroup, Products products) {
        if (create.getMenuProducts().size() != products.size()) {
            throw new ProductNotExistException();
        }

        Menu menu = new Menu();
        menu.name = create.getName();
        menu.price = create.getPrice();
        menu.menuGroup = menuGroup;

        create.getMenuProducts()
                .stream()
                .map(item -> new MenuProduct(menu, products.findById(item.getProductId()), item.getQuantity()))
                .forEach(item -> menu.menuProducts.add(item));

        Price amount = menu.menuProducts.sumAmount();
        if (create.getPrice().getPrice().compareTo(amount.getPrice()) > 0) {
            throw new MenuCheapException();
        }

        return menu;
    }

    public Menu() {
    }

    public Menu(Long id, String name, Price price, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts = new MenuProducts(menuProducts);
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
        return menuProducts.toCollection();
    }
}
