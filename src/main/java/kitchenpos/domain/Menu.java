package kitchenpos.domain;

import static javax.persistence.GenerationType.*;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.dto.ProductQuantityPair;
import kitchenpos.exception.InvalidMenuPriceException;

@Entity
public class Menu {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(Price price, Name name, MenuGroup menuGroup, List<ProductQuantityPair> pairs) {
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

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    private void validatePrice(Price price, List<ProductQuantityPair> pairs) {
        Price sumPriceOfProducts = pairs.stream()
            .map(ProductQuantityPair::sumOfPrice)
            .reduce(Price::add)
            .orElseThrow(RuntimeException::new);
        if (sumPriceOfProducts.lessThan(price)) {
            throw new InvalidMenuPriceException();
        }
    }

    public List<ProductQuantityPair> getProductQuantityPairs() {
        return this.menuProducts.getProductQuantityPairs();
    }
}
