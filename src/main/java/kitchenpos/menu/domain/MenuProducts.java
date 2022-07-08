package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "seq", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private final List<MenuProduct> collections;

    protected MenuProducts() {
        collections = new ArrayList<>();
    }

    public MenuProducts(final List<MenuProduct> collections) {
        this.collections = collections;
    }

    public static MenuProducts of(final List<MenuProduct> collections) {
        return new MenuProducts(collections);
    }

    public void add(final MenuProduct menuProduct) {
        collections.add(menuProduct);
    }

    public List<MenuProduct> getProducts() {
        return Collections.unmodifiableList(collections);
    }

    @Override
    public String toString() {
        return "MenuProducts{" +
                "collections=" + collections +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuProducts that = (MenuProducts) o;
        return Objects.equals(collections, that.collections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collections);
    }

}
