package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    private List<OrderLineItemRequest> orderLineItems;

    @BeforeEach
    void setUp() {
        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 3);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(1L, 3);

        orderLineItems = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
    }

    @Test
    @DisplayName("주문은 주문항목이 없으면 주문을 할 수 없다")
    void orderItemEmptyCreateOrder() {
        //given
        OrderRequest order = new OrderRequest(1L, new ArrayList<>());

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderValidator.createValidation(order)
        );
    }

    @Test
    @DisplayName("등록되지 않은 메뉴를 주문하면 주문을 할 수 없다.")
    void registerNotMenuOrder() {
        //given
        OrderRequest order = new OrderRequest(1L, orderLineItems);
        given(orderTableRepository.findByIdAndEmptyIsFalse(1L)).willReturn(Optional.of(new OrderTable(3, false)));

        //when & then
        assertThatThrownBy(() -> orderValidator.createValidation(order))
                .isInstanceOf(NoSuchElementException.class);
    }
    //
    @Test
    @DisplayName("존재하지 않은 테이블에 주문 할 수 없다.")
    void existNotOrderTable() {
        //gvien
        OrderRequest order = new OrderRequest(1L, orderLineItems);
        given(orderTableRepository.findByIdAndEmptyIsFalse(1L)).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> orderValidator.createValidation(order))
                .isInstanceOf(NoSuchElementException.class);

    }

}
