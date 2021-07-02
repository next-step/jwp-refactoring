package kitchenpos.domain.menu;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.domain.menuproduct.MenuProducts;
import kitchenpos.domain.product.Products;
import kitchenpos.exception.MenuCheapException;
import kitchenpos.exception.ProductNotExistException;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Name name;
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    private MenuProducts menuProducts = new MenuProducts();


    public static Menu create(MenuCreate create, MenuGroup menuGroup, Products products) {
        if (create.getMenuProducts().size() != products.size()) {
            throw new ProductNotExistException();
        }

        return new Menu(create.getName(), create.getPrice(),
                menuGroup, MenuProducts.create(create.getMenuProducts(), products));
    }

    protected Menu() {
    }

    public Menu(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(Name name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(id, new Name(name), price, menuGroup, menuProducts);
    }

    public Menu(Long id, Name name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProducts, this);

        validateAmount();
    }

    private void validateAmount() {
        Price amount = this.menuProducts.sumAmount();
        if (getPrice().compareTo(amount) > 0) {
            throw new MenuCheapException();
        }
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.toCollection();
    }
}
