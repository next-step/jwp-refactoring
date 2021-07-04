package kitchenpos.domain;

import static kitchenpos.domain.ProductTest.*;

public class MenuProductTest {

    public static final MenuProduct 행복세트_치킨 = new MenuProduct(1L, 1L, 치킨.getId(), 1);
    public static final MenuProduct 행복세트_피자 = new MenuProduct(2L, 1L, 피자.getId(), 1);
    public static final MenuProduct 행복세트_떡볶이 = new MenuProduct(3L, 1L, 떡볶이.getId(), 1);

    public static final MenuProduct 치쏘세트_치킨 = new MenuProduct(4L, 2L, 치킨.getId(), 1);
    public static final MenuProduct 치쏘세트_소주 = new MenuProduct(5L, 2L, 소주.getId(), 1);

    public static final MenuProduct 피맥세트_피자 = new MenuProduct(6L, 3L, 피자.getId(), 1);
    public static final MenuProduct 피맥세트_맥주 = new MenuProduct(7L, 3L, 맥주.getId(), 1);

}
