package kitchenpos.fixture;

import kitchenpos.domain.Menu;

import static java.math.BigDecimal.valueOf;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_치킨류;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_치킨_세트_리스트;

public class MenuFixture {
    public static Menu 메뉴_치킨_세트 = new Menu(1L, "후라이드치킨, 양념치킨 세트", valueOf(36_000), 메뉴그룹_치킨류.getId(), 메뉴_상품_치킨_세트_리스트);
}
