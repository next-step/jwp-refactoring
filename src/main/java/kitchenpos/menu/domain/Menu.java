package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private long price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu(String name, long price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Menu(Long id, String name, long price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    protected Menu() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return this.menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts.getAll();
    }

    public void add(Product product, long quantity) {
        this.menuProducts.add(new MenuProduct(this, product, quantity));
    }

    public void checkPrice() {
        if (this.price > this.menuProducts.totalPrice()) {
            throw new IllegalArgumentException();
        }
    }
}
