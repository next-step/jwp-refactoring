package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @Transient
    private Long menuGroupId;

    private MenuProducts menuProducts = new MenuProducts();


    public static Menu create(MenuCreate create, MenuGroup menuGroup, Products products) {
        Menu menu = new Menu();
        menu.name = create.getName();
        menu.price = create.getPrice();
        menu.menuGroup = menuGroup;

        if (create.getMenuProducts().size() != products.size()) {
            throw new IllegalArgumentException();
        }

        create.getMenuProducts()
                .stream()
                .map(item -> new MenuProduct(menu, products.findById(item.getProductId()), item.getQuantity()))
                .forEach(item -> menu.menuProducts.add(item));

        Price amount = menu.menuProducts.sumAmount();
        if (create.getPrice().getPrice().compareTo(amount.getPrice()) > 0) {
            throw new IllegalArgumentException();
        }

        return menu;
    }

    public Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(id, name, new Price(price), menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.toCollection();
    }
}
