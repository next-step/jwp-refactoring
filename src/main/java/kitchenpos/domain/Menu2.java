package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kitchenpos.exception.InvalidMenuPriceException;

@Entity
@Table(name = "menu")
public class Menu2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Money price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct2> menuProducts = new ArrayList<>();

    protected Menu2() {
    }

    public Menu2(String name, Long price, MenuGroup menuGroup, List<Product> products) {
        this.name = name;
        this.price = Money.valueOf(price);
        this.menuGroup = menuGroup;
        addMenuProducts(toMenuProduct(products));
    }

    public Menu2(String name, Long price, MenuGroup menuGroup, Map<Product, Integer> productsCount) {
        this.id = null;
        this.name = name;
        this.price = Money.valueOf(price);
        this.menuGroup = menuGroup;
        addMenuProducts(toMenuProducts(productsCount));
    }

    private List<MenuProduct2> toMenuProducts(Map<Product, Integer> productsCount) {
        return productsCount.entrySet()
            .stream()
            .map(entry -> new MenuProduct2(this, entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    private List<MenuProduct2> toMenuProduct(List<Product> products) {
        return products.stream()
            .map(product -> new MenuProduct2(this, product, 1))
            .collect(Collectors.toList());
    }

    private void addMenuProducts(List<MenuProduct2> menuProducts) {
        menuProducts.forEach(this::addMenuProduct);
    }

    private void addMenuProduct(MenuProduct2 addMenuProduct) {
        this.menuProducts.add(addMenuProduct);
        addMenuProduct.setMenu(this);
    }

    public List<MenuProduct2> getMenuProducts() {
        return menuProducts;
    }

    public Money getPrice() {
        return price;
    }

    public void validatePrice() {
        Money purchasePrice = sumAllPurchasePrices();
        if (price.isGreaterThan(purchasePrice)) {
            throw new InvalidMenuPriceException(price, purchasePrice);
        }
    }

    private Money sumAllPurchasePrices() {
        return menuProducts.stream()
            .map(MenuProduct2::getPurchasePrice)
            .reduce(Money.ZERO, Money::add);
    }
}
