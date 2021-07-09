package kitchenpos.order.domain;

import static kitchenpos.util.TestDataSet.원플원_양념;
import static kitchenpos.util.TestDataSet.원플원_후라이드;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.product.constant.OrderStatus;
import kitchenpos.table.domain.OrderTable;

public class OrderTest {

    private OrderRequest request;
    private OrderTable 테이블;
    private List<Menu> menuList;

    @BeforeEach
    void setUp() {
        테이블 = new OrderTable(1L, 1L, 10, false, null);
        request = new OrderRequest(1L, OrderStatus.COOKING,
            Arrays.asList(new OrderLineItemRequest(1L, 3L), new OrderLineItemRequest(2L, 3L)));
        menuList = Arrays.asList(원플원_후라이드, 원플원_양념);

    }

    @Test
    @DisplayName("정상 생성 케스트 테스트")
    void creat() {
        Order order = Order.create(테이블.getId());
        assertThat(order.getOrderTableId()).isEqualTo(테이블.getId());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

}
