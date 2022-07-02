package kitchenpos.service.order;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.service.order.application.OrdersValidator;
import kitchenpos.service.order.dto.OrderLineItemRequest;
import kitchenpos.service.order.dto.OrdersRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrdersValidatorTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private OrdersValidator ordersValidator;

    @Test
    @DisplayName("주문 항목의 요청 갯수와 실제 저장된 주문 항목을 조회했을 때 갯수가 다르면 주문에 실패한다.")
    void validate_orderLineItem_fail_1() {
        //given
        given(menuRepository.countByIdIn(any())).willReturn(2L);

        //then
        assertThatThrownBy(() -> ordersValidator.validate(
                new OrdersRequest(0L, Arrays.asList(new OrderLineItemRequest(0L, 1))))).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목의 요청 갯수가 없으면 주문에 실패한다.")
    void validate_orderLineItem_fail_2() {
        //then
        assertThatThrownBy(
                () -> ordersValidator.validate(new OrdersRequest(0L, Collections.emptyList()))).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 조회 결과가 없으면 주문에 실패한다.")
    void validate_orderTable_fail_1() {
        //given
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findByIdAndEmptyIsFalse(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> ordersValidator.validate(
                new OrdersRequest(0L, Arrays.asList(new OrderLineItemRequest(0L, 1))))).isExactlyInstanceOf(
                NoSuchElementException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있으면 주문에 실패한다.")
    void validate_orderTable_fail_2() {
        //given
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findByIdAndEmptyIsFalse(any())).willReturn(
                Optional.of(OrderTable.of( 1, true)));

        //then
        assertThatThrownBy(() -> ordersValidator.validate(
                new OrdersRequest(0L, Arrays.asList(new OrderLineItemRequest(0L, 1))))).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

}
