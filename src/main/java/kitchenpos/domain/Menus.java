package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Menus {
    @OneToMany(mappedBy = "menuGroup")
    private List<Menu> menus = new ArrayList<>();

    public <T> List<T> mapList(Function<Menu, T> function) {
        return menus.stream()
            .map(function)
            .collect(Collectors.toList());
    }
}
