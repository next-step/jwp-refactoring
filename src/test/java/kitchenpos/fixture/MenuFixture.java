package kitchenpos.fixture;

import kitchenpos.domain.Menu;

import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.asList;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_서브메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_치킨류;
import static kitchenpos.fixture.MenuProductFixture.*;

public class MenuFixture {
    public static Menu 메뉴_후라이드_치킨_한마리 = new Menu(1L, "후라이드 치킨 한마리", valueOf(18_000), 메뉴그룹_치킨류.getId(), asList(메뉴_상품_후라이드_치킨));
    public static Menu 메뉴_후라이드_치킨_두마리 = new Menu(2L, "양념 치킨 한마리", valueOf(18_000), 메뉴그룹_치킨류.getId(), asList(메뉴_상품_양념_치킨));
    public static Menu 메뉴_양념_후라이드_두마리_치킨_세트 = new Menu(3L, "양념, 후라이드 두마리 치킨 세트", valueOf(36_000), 메뉴그룹_치킨류.getId(), 메뉴_상품_양념_후라이드_두마리_치킨_세트);
    public static Menu 메뉴_양념_두마리_치킨_세트 = new Menu(4L, "양념 두마리 치킨 세트", valueOf(36_000), 메뉴그룹_치킨류.getId(), 메뉴_상품_양념_두마리_치킨_세트);
    public static Menu 메뉴_후라이드_두마리_치킨_세트 = new Menu(5L, "후라이드 두마리 치킨 세트", valueOf(36_000), 메뉴그룹_치킨류.getId(), 메뉴_상품_후라이드_두마리_치킨_세트);
    public static Menu 메뉴_감자튀김 = new Menu(6L, "감자튀김", valueOf(2000), 메뉴그룹_서브메뉴.getId(), asList(메뉴_상품_감자튀김));
    public static Menu 메뉴_치즈볼 = new Menu(7L, "치즈볼", valueOf(4000), 메뉴그룹_서브메뉴.getId(), asList(메뉴_상품_치즈볼));
    public static Menu 메뉴_옛날통닭 = new Menu(8L, "옛날통닭", valueOf(14_000), 메뉴그룹_치킨류.getId(), asList(메뉴_상품_옛날통닭));
}
