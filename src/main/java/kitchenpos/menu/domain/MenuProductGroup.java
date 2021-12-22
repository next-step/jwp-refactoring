package kitchenpos.menu.domain;

import kitchenpos.menu.exception.IllegalMenuProductException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class MenuProductGroup {
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProductGroup() {
    }

    private MenuProductGroup(List<MenuProduct> menuProducts) {
        validate(menuProducts);
        this.menuProducts.addAll(menuProducts);
    }

    private void validate(List<MenuProduct> menuProducts) {
        if (Objects.isNull(menuProducts) || menuProducts.isEmpty()) {
            throw new IllegalMenuProductException("상품 메뉴는 비어있을 수 없습니다.");
        }
    }

    public static MenuProductGroup of(List<MenuProduct> menuProducts) {
        return new MenuProductGroup(menuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
