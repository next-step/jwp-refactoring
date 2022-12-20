package kitchenpos.order.domain;

import static kitchenpos.menu.domain.MenuTestFixture.짜장_탕수육_세트;
import static kitchenpos.menu.domain.MenuTestFixture.짬뽕2_탕수육_세트;

public class OrderMenuTestFixture {

    public static OrderMenu 짜장_탕수육_주문_세트 = OrderMenu.of(짜장_탕수육_세트.id(), 짜장_탕수육_세트.name(), 짜장_탕수육_세트.price());
    public static OrderMenu 짬뽕2_탕수육_주문_세트 = OrderMenu.of(짬뽕2_탕수육_세트.id(), 짬뽕2_탕수육_세트.name(), 짬뽕2_탕수육_세트.price());
}
