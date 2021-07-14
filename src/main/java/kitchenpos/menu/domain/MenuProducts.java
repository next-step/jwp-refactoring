package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "menuId")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public void add(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public <R> List<R> convertAll(Function<MenuProduct, R> converter) {
        return this.menuProducts.stream()
                                .map(converter)
                                .collect(Collectors.toList());
    }
}
