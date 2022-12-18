package kitchenpos.order.domain;

import java.math.BigDecimal;

public class OrderMenuTest {

    public static OrderMenu 주문메뉴_생성(Long menuId, String menuName, BigDecimal menuPrice) {
        return new OrderMenu.Builder()
                .menuId(menuId)
                .menuName(menuName)
                .menuPrice(menuPrice)
                .build();
    }
}