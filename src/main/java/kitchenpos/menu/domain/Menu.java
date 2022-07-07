package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, Integer price) {
        this.name=name;
        this.price=Price.from(price);
    }

    public Menu(String name, Integer price, MenuGroup menuGroup) {
        this(name,price);
        this.menuGroup = menuGroup;
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        validPrice(menuProducts);
        this.menuProducts.addAll(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));

    }

    private void validPrice(List<MenuProduct> menuProducts) {
        BigDecimal productAmount = menuProducts.stream()
                .map(MenuProduct::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        if (price.biggerThan(productAmount)) {
            throw new IllegalArgumentException();
        }
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

    public BigDecimal getPriceBigDecimal() {
        return price.getValue();
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
