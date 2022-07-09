package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductResponse;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<MenuProduct> value = new ArrayList<>();

    public MenuProducts(Collection<MenuProduct> menuProducts) {
        this.value.addAll(menuProducts);
    }

    protected MenuProducts() {}

    public void addMenuProducts(MenuProducts menuProducts) {
        this.value.addAll(menuProducts.value);
    }

    public List<MenuProductResponse> toProductResponses() {
        return this.value.stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
    }

    public void associateMenu(Menu menu) {
        this.value.forEach(menuProduct -> menuProduct.associateMenu(menu));
    }

    public List<MenuProduct> getValue() {
        return value;
    }
}
