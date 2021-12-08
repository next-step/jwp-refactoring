package kitchenpos.acceptance.fixture;

import java.math.BigDecimal;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

public class Fixture {
    public static MenuGroup 사이드메뉴 = new MenuGroup();

    public static Product 참치맛감자튀김 = new Product();
    public static Product 고등어맛감자튀김 = new Product();

    public static Menu 신메뉴 = new Menu();

    static {
        사이드메뉴.setName("사이트메뉴");

        참치맛감자튀김.setName("참치맛감자튀김");
        참치맛감자튀김.setPrice(BigDecimal.valueOf(2_000).setScale(2));
        
        고등어맛감자튀김.setName("고등어맛감자튀김");
        고등어맛감자튀김.setPrice(BigDecimal.valueOf(2_000).setScale(2));

        신메뉴.setName("감튀세상");
        신메뉴.setPrice(BigDecimal.valueOf(3_000).setScale(2));
    }
}
