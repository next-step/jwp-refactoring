package kitchenpos.table.service;

import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.order.OrderFactory;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void 주문_테이블_생성() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(14, false);
        OrderTable orderTable = orderTableRequest.toOrderTable();

        given(orderTableRepository.save(orderTableRequest.toOrderTable())).willReturn(orderTable);

        // when
        OrderTableResponse response = tableService.create(orderTableRequest);

        // then
        assertThat(response.getNumberOfGuests()).isEqualTo(14);
        assertThat(response.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void 주문_테이블_목록_조회() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(14, false);
        OrderTable orderTable = orderTableRequest.toOrderTable();
        given(orderTableRepository.findAll()).willReturn(Collections.singletonList(orderTable));

        // when
        List<OrderTableResponse> response = tableService.list();

        // then
        assertThat(response.size()).isEqualTo(1);
    }

    @DisplayName("주문 테이블을 빈 테이블 여부를 변경한다.")
    @Test
    void 주문_테이블_빈_테이블_여부_변경() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);
        OrderTable orderTable = OrderTable.of(10, true);

        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        // when
        OrderTableResponse response = tableService.changeEmpty(orderTable.getId(), orderTableRequest);

        // then
        assertThat(response.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블이 특정 테이블 그룹에 속해 있으면 변경할 수 없다.")
    @Test
    void 빈_테이블_여부_변경_테이블_그룹_존재_예외() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);
        OrderTable orderTable = OrderTable.of(10, true);
        orderTable.addGroup(1L);

        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        // when
        Throwable thrown = catchThrowable(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest));

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("테이블 그룹이 존재하므로 빈 테이블 설정을 할 수 없습니다.");
    }

    @DisplayName("주문의 상태가 `조리` 또는 `식사`인 주문이 한 건 이상 있을 경우 변경할 수 없다.")
    @Test
    void 주문_상태_조리_또는_식사_중_변경_불가_예외() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);
        OrderTable orderTable = OrderTable.of(10, true);
        Order firstOrder = Order.of(orderTable.getId(), Collections.singletonList(OrderLineItem.of(1L, Quantity.of(1L))));
        Order secondOrder = Order.of(orderTable.getId(), Collections.singletonList(OrderLineItem.of(2L, Quantity.of(1L))));
        secondOrder.changeOrderStatus(OrderStatus.COMPLETION);

        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderRepository.findOrderByOrderTableId(orderTable.getId())).willReturn(Arrays.asList(firstOrder, secondOrder));

        // when
        Throwable thrown = catchThrowable(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest));

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("현재 테이블은 주문 완료 상태가 아니므로 빈 테이블 설정을 할 수 없습니다.");
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void 주문_테이블_손님_수_변경() {
        // given
        Long requestOrderTableId = 1L;
        OrderTableRequest 주문_테이블_요청 = OrderFactory.ofOrderTableRequest(10, false);
        OrderTable 저장된_주문_테이블 = OrderTable.of(1, false);
        given(orderTableRepository.findById(requestOrderTableId)).willReturn(Optional.of(저장된_주문_테이블));

        // when
        OrderTableResponse response = tableService.changeNumberOfGuests(requestOrderTableId, 주문_테이블_요청);

        // then
        assertThat(response.getNumberOfGuests()).isEqualTo(10);
    }
}
