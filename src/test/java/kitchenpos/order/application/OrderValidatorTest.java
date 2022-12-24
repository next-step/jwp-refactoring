package kitchenpos.order.application;

import static kitchenpos.fixture.OrderTableFixture.*;
import static kitchenpos.order.domain.OrderFixture.*;
import static kitchenpos.order.domain.OrderLineItemFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTableRepository;

@DisplayName("주문 유효성 검사")
@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @DisplayName("주문 등록 유효성 검사 - 등록 되어 있지 않은 주문 테이블")
    @Test
    void create_order_table_not_exists() {
        // given
        OrderRequest orderRequest = orderRequest(1L, Arrays.asList(
            orderLineItemRequest(1L, 1L),
            orderLineItemRequest(2L, 2L))
        );
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderValidator.validateSave(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 유효성 검사 - 빈 주문 항목")
    @Test
    void create_order_line_items_is_empty() {
        // given
        OrderRequest orderRequest = orderRequest(1L, Collections.emptyList());
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(savedOrderTable(1L, false)));

        // when, then
        assertThatThrownBy(() -> orderValidator.validateSave(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 유효성 검사 - 주문 테이블 빈 테이블")
    @Test
    void create_order_table_is_empty() {
        // given
        OrderRequest orderRequest = orderRequest(1L, Arrays.asList(
            orderLineItemRequest(1L, 1L),
            orderLineItemRequest(2L, 2L)
        ));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(savedOrderTable(1L, true)));

        // when, then
        assertThatThrownBy(() -> orderValidator.validateSave(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
