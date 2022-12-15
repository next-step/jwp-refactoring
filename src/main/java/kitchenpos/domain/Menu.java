package kitchenpos.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProduct) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProduct);
    }


    public static Menu of(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProduct) {
        return new Menu(name, price, menuGroup, menuProduct);
    }

    public Long getId() {
        return id;
    }
}
