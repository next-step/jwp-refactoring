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

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드치킨");
        menu.setPrice(new BigDecimal(16000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(asList(후라이드_1개()));

        return menu;
    }

    public static Menu 메뉴_양념치킨() {

        Menu menu = new Menu();
        menu.setId(2L);
        menu.setName("양념치킨");
        menu.setPrice(new BigDecimal(16000));
        menu.setMenuGroupId(2L);
        menu.setMenuProducts(asList(양념치킨_1개()));

        return menu;
    }

    public static Menu 메뉴_반반치킨() {

        Menu menu = new Menu();
        menu.setId(3L);
        menu.setName("반반치킨");
        menu.setPrice(new BigDecimal(16000));
        menu.setMenuGroupId(2L);
        menu.setMenuProducts(asList(반반치킨_1개()));

        return menu;
    }

    public static Menu 메뉴_가격이없는_반반치킨() {

        Menu menu = new Menu();
        menu.setId(4L);
        menu.setName("가격이 없는 반반치킨");
        menu.setPrice(new BigDecimal(-1));
        menu.setMenuGroupId(3L);
        menu.setMenuProducts(asList(가격이없는_반반치킨_1개()));

        return menu;
    }
}
