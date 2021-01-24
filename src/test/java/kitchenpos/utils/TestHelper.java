package kitchenpos.utils;

import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.product.dto.ProductRequest;

import java.math.BigDecimal;

public class TestHelper {
    public static final Long 등록된_menuGroup_id = 2L;
    public static final Long 등록되어_있지_않은_menuGroup_id = 5L;
    public static final Long 등록된_product_id = 1L;
    public static final Long 등록되어_있지_않은_product_id = 7L;
    public static final Long 등록된_menu_id = 1L;
    public static final Long 등록되어_있지_않은_menu_id = 7L;
    public static final Long 빈_orderTable_id1 = 1L;
    public static final Long 빈_orderTable_id2 = 2L;
    public static final Long 비어있지_않은_orderTable_id = 3L;
    public static final Long 등록되어_있지_않은_orderTable_id = 4L;

    public static final MenuGroupRequest 등록된_menuGroup =
            new MenuGroupRequest(등록된_menuGroup_id, "한마리메뉴");
    public static final MenuGroupRequest 등록되어_있지_않은_menuGroup =
            new MenuGroupRequest(등록되어_있지_않은_menuGroup_id, "핫메뉴");

    public static final ProductRequest 등록된_product =
            new ProductRequest(등록된_product_id, "후라이드", BigDecimal.valueOf(16000));
    public static final ProductRequest 등록되어_있지_않은_product =
            new ProductRequest(등록되어_있지_않은_product_id, "스노윙치킨", BigDecimal.valueOf(18000));
}
