package kitchenpos.menu.domain;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private List<MenuProduct> menuProducts;


    protected MenuProducts() {

    }
    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void registerMenuProduct(Menu menu, Price price) {
        validatePrice(price);
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
    }

    private void validatePrice(Price price) {
        long priceSum = menuProducts.stream().
            mapToLong(menuProduct -> menuProduct.getProduct().getPrice() * menuProduct.getQuantity()).
            sum();
        if (price.isGreaterThan(priceSum)) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void add(MenuProduct menuProduct){
        menuProducts.add(menuProduct);
    }
}
