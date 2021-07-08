package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public void forEach(Consumer<MenuProduct> consumer) {
        menuProducts.forEach(consumer);
    }

    public <T> List<T> mapList(Function<MenuProduct, T> function) {
        return menuProducts.stream()
            .map(function)
            .collect(Collectors.toList());
    }
}
