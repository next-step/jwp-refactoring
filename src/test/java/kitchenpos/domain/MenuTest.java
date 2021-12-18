package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.domain.MenuGroupTest.와퍼_그룹;
import static kitchenpos.domain.MenuProductTest.콜라;
import static kitchenpos.domain.MenuProductTest.통새우와퍼;

public class MenuTest {
    public static final Menu 통새우와퍼_세트 = new Menu(
            1L
            , "통새우와퍼 세트"
            , BigDecimal.valueOf(6000)
            , 와퍼_그룹
            , Arrays.asList(통새우와퍼, 콜라));

    public static final Menu 콜라_단품 = new Menu(
            2L
            , "콜라 단품"
            , BigDecimal.valueOf(1000)
            , 와퍼_그룹
            , Arrays.asList(콜라));
}
