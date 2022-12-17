package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderLineItemTestFixture.orderLineItemRequest;
import static kitchenpos.order.domain.OrderTestFixture.orderRequest;
import static kitchenpos.table.domain.OrderTableTestFixture.orderTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 유효성 검사 테스트")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderValidator orderValidator;

    @Test
    @DisplayName("주문 등록시 주문 항목은 모두 등록된 메뉴여야 한다.")
    void createOrderByCreatedMenu() {
        // given
        OrderTable orderTable = orderTable(1L, null, 3, false);
        OrderLineItemRequest orderLineItemRequest = orderLineItemRequest(1L, 1L);
        OrderLineItemRequest orderLineItemRequest2 = orderLineItemRequest(2L, 1L);
        OrderLineItemRequest 탕수육_1그릇_요청 = orderLineItemRequest(null, 1L);
        OrderRequest orderRequest = orderRequest(orderTable.id(), Arrays.asList(orderLineItemRequest, orderLineItemRequest2, 탕수육_1그릇_요청));
        given(orderTableRepository.findById(orderTable.id())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(orderRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("등록되지 않은 메뉴가 있습니다.");
    }

    @Test
    @DisplayName("주문 등록시 주문 테이블은 등록된 테이블이어야 한다.")
    void createOrderByCreatedOrderTable() {
        // given
        OrderTable orderTable = orderTable(1L, null, 3, false);
        OrderLineItemRequest orderLineItemRequest = orderLineItemRequest(1L, 1L);
        OrderLineItemRequest orderLineItemRequest2 = orderLineItemRequest(2L, 1L);
        given(orderTableRepository.findById(orderTable.id())).willReturn(Optional.empty());
        OrderRequest orderRequest = orderRequest(orderTable.id(), Arrays.asList(orderLineItemRequest, orderLineItemRequest2));

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(orderRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다. ID : 1");
    }

    @Test
    @DisplayName("주문 등록시 주문 테이블은 비어있는 테이블일 수 없다.")
    void createOrderByEmptyOrderTable() {
        // given
        OrderLineItemRequest orderLineItemRequest = orderLineItemRequest(1L, 1L);
        OrderLineItemRequest orderLineItemRequest2 = orderLineItemRequest(2L, 1L);
        OrderTable emptyTable = orderTable(2L, null, 0, true);
        given(orderTableRepository.findById(emptyTable.id())).willReturn(Optional.of(emptyTable));
        OrderRequest orderRequest = orderRequest(emptyTable.id(), Arrays.asList(orderLineItemRequest, orderLineItemRequest2));

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(orderRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("비어있는 테이블입니다.");
    }
}
