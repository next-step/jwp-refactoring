package kichenpos.order.product.domain;

import kichenpos.common.domain.Name;
import kichenpos.common.domain.Price;
import org.springframework.util.Assert;

public final class Menu {

    private final long id;
    private final Name name;
    private final Price price;

    private Menu(long id, Name name, Price price) {
        Assert.notNull(name, "이름은 필수입니다.");
        Assert.notNull(price, "가격은 필수입니다.");
        this.id = id;
        this.price = price;
        this.name = name;
    }

    public static Menu of(long id, Name name, Price price) {
        return new Menu(id, name, price);
    }

    public long id() {
        return id;
    }

    public Name name() {
        return name;
    }

    public Price price() {
        return price;
    }
}
