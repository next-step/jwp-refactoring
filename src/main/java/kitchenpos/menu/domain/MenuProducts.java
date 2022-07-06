package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> elements = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts, Price price, Menu menu) {
        elements = new ArrayList<>(menuProducts);
        validatePrice(price);
        addMenu(menu);
    }

    public List<MenuProduct> elements() {
        return Collections.unmodifiableList(elements);
    }

    private void addMenu(Menu menu) {
        elements
                .forEach(menuProduct -> menuProduct.updateMenu(menu));
    }

    private void validatePrice(Price menuPrice) {
        if (menuPrice.isExpensive(sumOfMenuProductAmounts())) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품 가격의 총합보다 높을 수 없습니다.");
        }
    }

    private Price sumOfMenuProductAmounts() {
        Price price = new Price();
        for (MenuProduct element : elements) {
            price = price.add(element.price());
        }
        return price;
    }
}
