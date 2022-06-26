package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> elements = new ArrayList<>();

    public List<MenuProduct> getElements() {
        return elements;
    }

    public void addAll(Menu menu, List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            elements.add(menuProduct);
            menuProduct.setMenu(menu);
        }

        if (menu.hasPriceGreaterThan(totalPrice())) {
            throw new IllegalArgumentException("메뉴 가격은 구성 상품 총 가격보다 클 수 없습니다.");
        }
    }

    private BigDecimal totalPrice() {
        return elements.stream()
                .map(menuProduct -> menuProduct.getPrice())
                .reduce(BigDecimal.ZERO, (acc, price) -> acc.add(price));
    }
}
