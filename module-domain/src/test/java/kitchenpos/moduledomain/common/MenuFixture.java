package kitchenpos.moduledomain.common;


import kitchenpos.moduledomain.menu.Menu;
import kitchenpos.moduledomain.menu.MenuGroup;
import kitchenpos.moduledomain.product.Amount;


public class MenuFixture {

    public static Menu 메뉴_양념치킨() {
        MenuGroup 메뉴그룹_한마리 = MenuGroupFixture.메뉴그룹_한마리();
        return Menu.of(1L, "양념치킨", Amount.of(16000), 메뉴그룹_한마리.getId());
    }

    public static Menu 메뉴_후라이드() {
        MenuGroup 메뉴그룹_한마리 = MenuGroupFixture.메뉴그룹_한마리();
        return Menu.of(2L, "후라이드치킨", Amount.of(16000), 메뉴그룹_한마리.getId());
    }

    public static Menu 메뉴_반반치킨() {
        MenuGroup 메뉴그룹_한마리 = MenuGroupFixture.메뉴그룹_한마리();
        return Menu.of(3L, "반반치킨", Amount.of(16000), 메뉴그룹_한마리.getId());
    }

    public static Menu 메뉴_가격이없는_반반치킨() {
        MenuGroup 메뉴그룹_한마리 = MenuGroupFixture.메뉴그룹_한마리();
        return Menu.of(4L, "가격이 없는 반반치킨", Amount.of(-1), 메뉴그룹_한마리.getId());
    }
}
