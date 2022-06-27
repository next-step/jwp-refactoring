package kitchenpos.menu.domain;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    public static final String DONT_EMPTY_IS_MENU_PRODUCTS = "메뉴 상품(menuProduct) 가 존재하지 않습니다.";

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> values = Lists.newArrayList();

    protected MenuProducts() {}

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.values = menuProducts;
    }

    public static MenuProducts createEmpty() {
        return new MenuProducts(Lists.newArrayList());
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        validateMenuProducts(menuProducts);
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getReadOnlyValues() {
        return Collections.unmodifiableList(this.values);
    }

    public void add(MenuProduct menuProduct) {
        this.values.add(menuProduct);
    }

    public void addAll(List<MenuProduct> menuProducts) {
        this.values.addAll(menuProducts);
    }

    private static void validateMenuProducts(List<MenuProduct> menuProducts) {
        if (menuProducts == null) {
            throw new IllegalArgumentException(DONT_EMPTY_IS_MENU_PRODUCTS);
        }
    }
}
