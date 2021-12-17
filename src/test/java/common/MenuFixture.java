package common;

import static common.MenuProductFixture.가격이없는_반반치킨_1개;
import static common.MenuProductFixture.반반치킨_1개;
import static common.MenuProductFixture.양념치킨_1개;
import static common.MenuProductFixture.후라이드_1개;
import static java.util.Arrays.asList;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;

public class MenuFixture {

    public static Menu 메뉴_후라이드() {
        return new Menu(1L, "후라이드치킨", new BigDecimal(16000), 1L, asList(후라이드_1개()));
    }

    public static Menu 메뉴_양념치킨() {
        return new Menu(2L, "양념치킨", new BigDecimal(16000), 2L, asList(양념치킨_1개()));
    }

    public static Menu 메뉴_반반치킨() {
        return new Menu(3L, "반반치킨", new BigDecimal(16000), 2L, asList(반반치킨_1개()));
    }

    public static Menu 메뉴_가격이없는_반반치킨() {
        return new Menu(4L, "가격이 없는 반반치킨", new BigDecimal(-1), 2L, asList(가격이없는_반반치킨_1개()));
    }
}
