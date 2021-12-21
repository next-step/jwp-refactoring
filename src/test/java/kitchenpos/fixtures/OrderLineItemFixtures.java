package kitchenpos.fixtures;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * packageName : kitchenpos.fixtures
 * fileName : OrderLineItemFixtures
 * author : haedoang
 * date : 2021/12/18
 * description :
 */
public class OrderLineItemFixtures {

    public static List<OrderLineItem> 주문정보_1개_수량_1개() {
        return Lists.newArrayList(new OrderLineItem(new Menu(), 1L));
    }
}
