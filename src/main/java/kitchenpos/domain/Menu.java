package kitchenpos.domain;

import static javax.persistence.GenerationType.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.dto.ProductQuantityPair;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.InvalidPriceException;

@Entity
public class Menu {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    private String name;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(BigDecimal price, String name, MenuGroup menuGroup, List<ProductQuantityPair> pairs) {
        validatePrice(price, pairs);

        for (ProductQuantityPair pair : pairs) {
            this.menuProducts.add(new MenuProduct(this, pair.getProduct(), pair.getQuantity()));
        }
        this.price = price;
        this.name = name;
        this.menuGroup = menuGroup;
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    private void validatePrice(BigDecimal price, List<ProductQuantityPair> pairs) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
        }
        Long sumPriceOfProducts = pairs.stream()
            .map(ProductQuantityPair::sumOfPrice)
            .reduce(Long::sum)
            .orElseThrow(RuntimeException::new);
        if (sumPriceOfProducts < price.longValue()) {
            throw new InvalidMenuPriceException();
        }
    }

    public List<ProductQuantityPair> getProductQuantityPairs() {
        return this.menuProducts.getProductQuantityPairs();
    }
}
