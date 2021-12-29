package kitchenpos.fixture;

import kitchenpos.menu.domain.*;

public class MenuFixture {
    public static Menu 메뉴_후라이드치킨 = Menu.of(ProductFixture.상품_후라이드치킨.getName(), ProductFixture.상품_후라이드치킨.getPrice(), MenuGroupFixture.메뉴그룹_한마리메뉴);
    public static Menu 메뉴_양념치킨 = Menu.of(ProductFixture.상품_양념치킨.getName(), ProductFixture.상품_양념치킨.getPrice(), MenuGroupFixture.메뉴그룹_한마리메뉴);
}
