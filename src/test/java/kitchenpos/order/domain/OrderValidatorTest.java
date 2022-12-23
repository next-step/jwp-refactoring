package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("주문요청검증기능")
public class OrderValidatorTest {

    @Test
    @DisplayName("제품 아이템은 비어있으면 안된다.")
    void orderValidatorTest1() {
        OrderRequest orderRequest = new OrderRequest(null, Arrays.asList());

        assertThatThrownBy(() -> OrderValidator.validateParam(orderRequest.getOrderLineItems()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하는 메뉴로만 요청해야한다.")
    void orderValidatorTest2() {
        OrderRequest orderRequest = new OrderRequest(null, Arrays.asList(new OrderLineItemRequest(1L, 1L)));

        assertThatThrownBy(() -> OrderValidator.validateMenus(orderRequest.getOrderLineItems(), 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블은 비어있으면 안된다.")
    void orderTest1() {
        OrderTable orderTable = OrderTable.create( 5, true);

        assertThatThrownBy(() -> OrderValidator.validateOrderTable(orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 존재여부 확인 기능")
    void orderTableTest1() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());

        assertThatThrownBy(() -> OrderValidator.validateGrouped(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비어있는지 확인 기능")
    void priceTest2() {
        assertThatThrownBy(() -> OrderValidator.validateEmpty(true)).isInstanceOf(IllegalArgumentException.class);
    }

}
