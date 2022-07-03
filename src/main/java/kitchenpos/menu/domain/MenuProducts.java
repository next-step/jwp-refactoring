package kitchenpos.menu.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> elements = new ArrayList<>();

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.elements = menuProducts;
    }

    public List<MenuProduct> getElements() {
        return Collections.unmodifiableList(new ArrayList<>(this.elements));
    }

    public void addMenu(Menu menu) {
        elements.forEach(menuProduct -> menuProduct.updateMenu(menu));
    }

    public void validatePrice(BigDecimal price) {
        if (price.compareTo(sum()) > 0) {
            throw new BadRequestException(ErrorCode.INVALID_MENU_PRICE);
        }
    }

    private BigDecimal sum() {
        BigDecimal price = BigDecimal.ZERO;
        for (MenuProduct element : elements) {
            price = price.add(element.calculateAmount());
        }
        return price;
    }
}
