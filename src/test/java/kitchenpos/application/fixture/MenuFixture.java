package kitchenpos.application.fixture;

import static kitchenpos.application.fixture.MenuGroupFixture.*;
import static kitchenpos.application.fixture.ProductFixture.*;

import java.math.BigDecimal;
import java.util.Arrays;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu 불고기 = new Menu();
    public static Menu 샐러드 = new Menu();

    public static MenuProduct 불고기_돼지고기 = new MenuProduct();
    public static MenuProduct 불고기_공기밥 = new MenuProduct();
    public static MenuProduct 샐러드_양상추 = new MenuProduct();
    public static MenuProduct 샐러드_닭가슴살 = new MenuProduct();

    static {
        // Menu
        불고기.setId(1L);
        불고기.setName("불고기");
        불고기.setPrice(BigDecimal.valueOf(10_000));
        불고기.setMenuGroupId(고기_메뉴그룹.getId());
        불고기.setMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        샐러드.setId(2L);
        샐러드.setName("샐러드");
        샐러드.setPrice(BigDecimal.valueOf(7_000));
        샐러드.setMenuGroupId(야채_메뉴그룹.getId());
        샐러드.setMenuProducts(Arrays.asList(샐러드_양상추, 샐러드_닭가슴살));

        // MenuProduct
        불고기_돼지고기.setSeq(1L);
        불고기_돼지고기.setMenuId(불고기.getId());
        불고기_돼지고기.setProductId(돼지고기.getId());
        불고기_돼지고기.setQuantity(1L);

        불고기_공기밥.setSeq(2L);
        불고기_공기밥.setMenuId(불고기.getId());
        불고기_공기밥.setProductId(공기밥.getId());
        불고기_공기밥.setQuantity(1L);

        샐러드_양상추.setSeq(3L);
        샐러드_양상추.setMenuId(샐러드.getId());
        샐러드_양상추.setProductId(양상추.getId());
        샐러드_양상추.setQuantity(1L);

        샐러드_닭가슴살.setSeq(4L);
        샐러드_닭가슴살.setMenuId(샐러드.getId());
        샐러드_닭가슴살.setProductId(닭가슴살.getId());
        샐러드_닭가슴살.setQuantity(1L);
    }

    private MenuFixture() {}
}
