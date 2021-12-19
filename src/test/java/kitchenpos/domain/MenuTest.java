package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.domain.MenuGroupTest.두마리메뉴;
import static kitchenpos.domain.MenuGroupTest.한마리메뉴;
import static kitchenpos.domain.MenuProductTest.양념치킨;
import static kitchenpos.domain.MenuProductTest.후라이드;

public class MenuTest {
    public static final Menu 치킨세트 = new Menu(
            1L
            , "치킨 세트"
            , BigDecimal.valueOf(30000)
            , 두마리메뉴
            , Arrays.asList(후라이드, 양념치킨));

    public static final Menu 양념치킨_단품 =new Menu(
            2L
            , "양념치킨 단품"
            , BigDecimal.valueOf(16000)
            , 한마리메뉴
            , Arrays.asList(양념치킨));
}
