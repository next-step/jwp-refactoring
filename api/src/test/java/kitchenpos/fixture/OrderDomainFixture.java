package kitchenpos.fixture;

import kitchenpos.order.domain.order.Order;

import static kitchenpos.fixture.OrderTableDomainFixture.한식_테이블;

public class OrderDomainFixture {

    public static Order 첫_주문 = new Order(한식_테이블);

}
