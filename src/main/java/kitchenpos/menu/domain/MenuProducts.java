package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuProductResponse;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
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

    public Price getTotalProductPrice() {
        return this.value.stream()
                .map(MenuProduct::getTotalPrice)
                .reduce(Price::add)
                .orElse(new Price(BigDecimal.ZERO));
    }

    public List<MenuProductResponse> toProductResponses() {
        return this.value.stream()
                .map(menuProduct -> MenuProductResponse.of(menuProduct.getProduct(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
    }
}
