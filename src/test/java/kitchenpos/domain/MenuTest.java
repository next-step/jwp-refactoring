package kitchenpos.domain;

import static kitchenpos.domain.MenuGroupTest.*;
import static kitchenpos.domain.MenuProductTest.*;

import java.math.BigDecimal;
import java.util.Arrays;

public class MenuTest {

    public static final Menu 행복세트 = new Menu(1L, "행복세트", BigDecimal.valueOf(17900), 추천메뉴.getId(),
        Arrays.asList(행복세트_치킨, 행복세트_피자, 행복세트_떡볶이));
    public static final Menu 치쏘세트 = new Menu(2L, "치쏘세트", BigDecimal.valueOf(8000), 세트메뉴.getId(),
        Arrays.asList(치쏘세트_치킨, 치쏘세트_소주));
    public static final Menu 피맥세트 = new Menu(3L, "피맥세트", BigDecimal.valueOf(11900), 세트메뉴.getId(),
        Arrays.asList(피맥세트_피자, 피맥세트_맥주));

}
