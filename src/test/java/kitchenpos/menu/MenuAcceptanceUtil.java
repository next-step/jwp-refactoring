package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

import static kitchenpos.menu.MenuAcceptanceTest.메뉴_등록됨;
import static kitchenpos.menugroup.MenuGroupAcceptanceTest.메뉴_그룹_등록됨;
import static kitchenpos.product.ProductAcceptanceTest.상품_등록됨;

public final class MenuAcceptanceUtil {

    private MenuAcceptanceUtil() {
    }

    public static Menu 신메뉴_강정치킨_가져오기() {
        Product 강정치킨 = 상품_등록됨("강정치킨", BigDecimal.valueOf(15_000L));
        MenuGroup 신메뉴 = 메뉴_그룹_등록됨("신메뉴");
        return 메뉴_등록됨("강정치킨", BigDecimal.valueOf(15_000L), 신메뉴.getId(), 강정치킨);
    }
}
