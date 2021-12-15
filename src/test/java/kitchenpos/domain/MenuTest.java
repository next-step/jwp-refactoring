package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.domain.MenuProductTest.콜라;
import static kitchenpos.domain.MenuProductTest.통새우와퍼;

public class MenuTest {
    public static final Menu 통새우와퍼_세트 = new Menu(
            1L
            ,"통새우와퍼 세트"
            , BigDecimal.valueOf(6000)
            , 1L
            , Arrays.asList(통새우와퍼, 콜라));
}
