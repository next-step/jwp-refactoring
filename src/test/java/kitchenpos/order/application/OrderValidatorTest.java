package kitchenpos.order.application;

import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @InjectMocks
    private OrderValidator orderValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Test
    void 빈_테이블에서_주문한_경우() {
        // given
        OrderTable orderTable = new OrderTable(1L, new TableGroup(1L), 0, true);
        OrderRequest request = new OrderRequest(1L, Collections.singletonList(new OrderLineItemRequest(1L, 1)));
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));


        // when & then
        assertThatThrownBy(() -> orderValidator.validateOrderTable(request.getOrderTableId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}