package common;

import static common.MenuGroupFixture.메뉴그룹_한마리;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Amount;

public class MenuFixture {

    public static Menu 메뉴_양념치킨() {
        return Menu.of(1L, "양념치킨", Amount.of(16000), 메뉴그룹_한마리());
    }

    public static Menu 메뉴_후라이드() {
        return Menu.of(2L, "후라이드치킨", Amount.of(16000), 메뉴그룹_한마리());
    }

    public static Menu 메뉴_반반치킨() {
        return Menu.of(3L, "반반치킨", Amount.of(16000), 메뉴그룹_한마리());
    }

    public static Menu 메뉴_가격이없는_반반치킨() {
        return Menu.of(4L, "가격이 없는 반반치킨", Amount.of(-1), 메뉴그룹_한마리());
    }
}
