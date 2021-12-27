package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

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

    public List<ProductId> getProductIds() {
        return this.menuProducts.stream()
                                .map(menuProduct -> menuProduct.getProductId())
                                .collect(Collectors.toList());
    }
}
