package kitchenpos.menu.domain;

import static kitchenpos.product.domain.ProductTest.*;

import org.junit.jupiter.api.DisplayName;

import kitchenpos.generic.price.domain.Price;
import kitchenpos.generic.quantity.domain.Quantity;

@DisplayName("메뉴 단위 테스트")
public class MenuTest {
    public static Menu 후라이드_메뉴 = new Menu(1L, "후라이드치킨", Price.valueOf(16000), MenuProducts.of(new MenuProduct(후라이드.getId(), Quantity.valueOf(1))));
    public static Menu 양념치킨_메뉴 = new Menu(2L, "양념치킨", Price.valueOf(16000), MenuProducts.of(new MenuProduct(양념치킨.getId(), Quantity.valueOf(1))));
    public static Menu 반반치킨_메뉴 = new Menu(3L, "반반치킨", Price.valueOf(16000), MenuProducts.of(new MenuProduct(반반치킨.getId(), Quantity.valueOf(1))));
    public static Menu 통구이_메뉴 = new Menu(4L, "통구이", Price.valueOf(16000), MenuProducts.of(new MenuProduct(통구이.getId(), Quantity.valueOf(1))));
    public static Menu 간장치킨_메뉴 = new Menu(5L, "간장치킨", Price.valueOf(16000), MenuProducts.of(new MenuProduct(간장치킨.getId(), Quantity.valueOf(1))));
    public static Menu 순살치킨_메뉴 = new Menu(6L, "순살치킨", Price.valueOf(16000), MenuProducts.of(new MenuProduct(순살치킨.getId(), Quantity.valueOf(1))));

    public static Menu menu(Long id, String name, Price price, MenuProducts menuProducts) {
        return new Menu(id, name, price, menuProducts);
    }
}
