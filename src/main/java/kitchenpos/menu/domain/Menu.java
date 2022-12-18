package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "menu")
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;

        for (MenuProduct menuProduct : menuProducts) {
            addMenuProduct(menuProduct);
        }

        validatePrice();
    }

    public void addMenuProduct(MenuProduct menuProduct){
        this.menuProducts.add(menuProduct);
        menuProduct.changeMenu(this);
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void validatePrice() {
        if(isPriceNull() || isPriceLessThanZero()){
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.calculatePrice());
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isPriceLessThanZero() {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isPriceNull() {
        return price == null;
    }
}
