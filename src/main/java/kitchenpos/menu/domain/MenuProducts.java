package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id")
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
    
    public int getTotalPrice(){
        return menuProducts.stream()
                .map(MenuProduct::createAmount)
                .mapToInt(Amount::getAmount)
                .sum();
    }
}
