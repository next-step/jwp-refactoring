package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.product.domain.Product;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @ManyToOne
    private MenuGroup menuGroup;
    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup,
                List<MenuProduct> menuProducts) {
        checkPrice(price);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts.addMenuProducts(menuProducts);
        this.menuProducts.includeToMenu(this);
    }

    private void checkPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuPriceException();
        }
    }

    public void checkSumPriceOfProducts(List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;

        for (final Product product : products) {
            MenuProduct menuProduct = findMenuProductByProductId(product.getId());
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new InvalidMenuPriceException();
        }
    }

    private MenuProduct findMenuProductByProductId(Long id) {
        return menuProducts.findMenuProductByProductId(id);
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.toList();
    }
}
