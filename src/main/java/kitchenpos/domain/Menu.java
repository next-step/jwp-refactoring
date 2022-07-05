package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @Column(nullable = false)
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        validateSumPrice(menuProducts);
        for (MenuProduct menuProduct : menuProducts) {
            if (!this.menuProducts.contains(menuProduct)) {
                this.menuProducts.add(menuProduct);
            }
            menuProduct.bindTo(this);
        }
    }

    private void validateSumPrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::calculateSumPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.isGatherThan(sum)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}
