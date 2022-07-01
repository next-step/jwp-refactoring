package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menu.exception.NotExistMenuProductException;
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

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup,
                List<MenuProduct> menuProducts) {
        checkPrice(price);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts.addAll(menuProducts);
        this.menuProducts.forEach(menuProduct -> menuProduct.includeToMenu(this));
    }

    private void checkPrice(BigDecimal price){
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

    private MenuProduct findMenuProductByProductId(Long id){
        return menuProducts.stream()
                .filter(menuProduct -> menuProduct.getProductId().equals(id))
                .findFirst()
                .orElseThrow(NotExistMenuProductException::new);
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
        return menuProducts;
    }
}
