package kitchenpos.menu.domain;

import kitchenpos.menuGroup.domain.MenuGroup;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {

    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroup() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getList();
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        menuProducts.stream()
                .forEach(menuProduct -> {
                    menuProduct.updateMenu(this);
                    this.menuProducts.add(menuProduct);
                });
        this.menuProducts.validationOverPrice(this.price);
    }

}
