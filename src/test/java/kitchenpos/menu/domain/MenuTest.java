package kitchenpos.menu.domain;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 도메인 단위 테스트")
public class MenuTest {

    private MenuGroup 한식;
    private Menu 한정식_A세트;
    private MenuProduct 불고기;
    private MenuProduct 용포탕;

    @BeforeEach
    void setUp() {
        한식 = new MenuGroup("한식");
        한정식_A세트 = new Menu("한정식 A세트", new BigDecimal(50000), 한식);
        불고기 = new MenuProduct(한정식_A세트, new Product("불고기", new BigDecimal(15000)), 1L);
        용포탕 = new MenuProduct(한정식_A세트, new Product("용포탕", new BigDecimal(35000)), 1L);
    }

    @Test
    void 동등성_테스트() {
        assertEquals(new Menu("한정식 A세트", new BigDecimal(50000), 한식),
                new Menu("한정식 A세트", new BigDecimal(50000), 한식));
    }
}
