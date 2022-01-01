package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.fixture.TestOrderFactory;
import kitchenpos.table.fixture.TestOrderTableFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableValidatorTest {
    @Mock
    private OrderService orderService;
    @InjectMocks
    private TableValidator tableValidator;

    @DisplayName("주문 목록이 존재하지 않으면 해지할 수 없다.")
    @Test
    void validateUnGroup() {
        final OrderTable 주문테이블 = TestOrderTableFactory.주문_테이블_조회됨(1L, 10, true);
        final OrderTable 빈_주문테이블 = TestOrderTableFactory.주문_테이블_조회됨(1L, 10, false);
        final List<OrderTable> 주문테이블_목록_조회됨 = Lists.newArrayList(주문테이블, 빈_주문테이블);
        final List<Order> 빈_주문_목록_조회됨 = new ArrayList<>();

        given(orderService.findByOrderTableIdIn(anyList())).willReturn(빈_주문_목록_조회됨);

        assertThatThrownBy(() -> tableValidator.validateUnGroup(주문테이블_목록_조회됨))
                .hasMessage("주문 정보가 존재하지 않습니다.");
    }

    @DisplayName("주문의 상태가 Cooking, Meal 상태가 있으면 해지할 수 없다.")
    @Test
    void validateUnGroup2() {
        final OrderTable 주문테이블1 = TestOrderTableFactory.주문_테이블_조회됨(1L, 10, true);
        final Order 주문1 = TestOrderFactory.주문_meal_조회됨(1, 1L, 주문테이블1.getId(), 1L, 5);

        final OrderTable 주문테이블2 = TestOrderTableFactory.주문_테이블_조회됨(2L, 10, true);
        final Order 주문2 = TestOrderFactory.주문_cooking_조회됨(1, 2L, 주문테이블2.getId(), 1L, 5);

        final List<OrderTable> 주문테이블_목록_조회됨 = Lists.newArrayList(주문테이블1, 주문테이블2);
        final List<Order> 주문_목록_조회됨 = Lists.newArrayList(주문1, 주문2);

        given(orderService.findByOrderTableIdIn(anyList())).willReturn(주문_목록_조회됨);

        assertThatThrownBy(() -> tableValidator.validateUnGroup(주문테이블_목록_조회됨))
                .hasMessage("식사 중이나 요리 중 상태가 아니어야 합니다.");
    }
}
