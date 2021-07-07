package kitchenpos.order.domain;

import static kitchenpos.util.TestDataSet.원플원_양념;
import static kitchenpos.util.TestDataSet.원플원_후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
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
import kitchenpos.table.domain.TableGroup;

public class OrderTest {

    private OrderRequest request;
    private OrderTable 테이블;
    private List<Menu> menuList;

    @BeforeEach
    void setUp() {
        테이블 = new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 10, false, null);
        request = new OrderRequest(1L, OrderStatus.COOKING,
            Arrays.asList(new OrderLineItemRequest(1L, 3L), new OrderLineItemRequest(2L, 3L)));
        menuList = Arrays.asList(원플원_후라이드, 원플원_양념);

    }

    @Test
    @DisplayName("정상 생성 케스트 테스트")
    void creat() {
        Order order = Order.create(request, 테이블, menuList);
        assertThat(order.getOrderTableId()).isEqualTo(테이블.getId());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("실페 케이스 -> 요청으로 들어온 list 수와 검생된 list수가 다를 때 ")
    void notMatch() {
        request = new OrderRequest(1L, OrderStatus.COOKING,
            Arrays.asList(new OrderLineItemRequest(1L, 3L)));
        assertThrows(IllegalArgumentException.class, () -> {
            Order.create(request, 테이블, menuList);
        });
    }

    @Test
    @DisplayName("실페 케이스 -> 테이블이 빈 상태일 때 ")
    void emptyTable() {
        테이블 = new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 10, true, null);
        assertThrows(IllegalArgumentException.class, () -> {
            Order.create(request, 테이블, menuList);
        });
    }
}
