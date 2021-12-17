package kitchenpos.domain.menu;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.domain.Price;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuProduct> menuProducts;

    protected MenuProducts() {
        menuProducts = new ArrayList<>();
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts= menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public Price getSumProductPrice() {
        Price sumOfProductsPrice = Price.of(0);

        for (MenuProduct menuProduct : this.menuProducts) {
            sumOfProductsPrice = sumOfProductsPrice.add(menuProduct.calculatePrice());
        }

        return sumOfProductsPrice;
    }

    public boolean remove(MenuProduct menuProduct) {
        return this.menuProducts.remove(menuProduct);
    }
    
    public boolean add(MenuProduct menuProduct) {
        return this.menuProducts.add(menuProduct);
    }

    public void acceptMenu(Menu menu) {
        for (MenuProduct menuProduct : this.menuProducts) {
            menuProduct.acceptMenu(menu);
        }
    }
    
    public int size() {
        return this.menuProducts.size();
    }

    public MenuProduct get(int index) {
        return this.menuProducts.get(index);
    }
}
