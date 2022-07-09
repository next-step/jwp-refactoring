package kitchenpos.table.application;

import static kitchenpos.order.__fixture__.OrderLineItemTestFixture.주문_항목_요청_생성;
import static kitchenpos.order.__fixture__.OrderTestFixture.빈_주문_요청_생성;
import static kitchenpos.order.__fixture__.OrderTestFixture.주문_요청_생성;
import static kitchenpos.table.__fixture__.OrderTableTestFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.infra.MenuRepository;
import kitchenpos.order.request.OrderLineItemRequest;
import kitchenpos.order.request.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.infra.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("OrderTableValidator 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderTableValidatorTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderTableValidator orderTableValidator;
    private OrderLineItemRequest 주문_항목_요청;
    private OrderRequest 주문_요청;

    @BeforeEach
    void setUp() {
        주문_항목_요청 = 주문_항목_요청_생성(1L, "후라이드치킨", BigDecimal.valueOf(16_000), 1L);
        주문_요청 = 주문_요청_생성(1L, Arrays.asList(주문_항목_요청));
    }

    @Test
    @DisplayName("주문 시 주문 항목이 비어있으면 Exception")
    public void createEmptyException() {
        final OrderRequest 빈_주문_요청 = 빈_주문_요청_생성(2L);

        Assertions.assertThatThrownBy(() -> orderTableValidator.validateOrderRequest(빈_주문_요청)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 시 주문 항목이 메뉴에 존재하지 않으면 Exception")
    public void createOrderLineItemsNotExistsException() {
        BDDMockito.given(menuRepository.countByIdIn(ArgumentMatchers.any(List.class))).willReturn(2L);
        Assertions.assertThatThrownBy(() -> orderTableValidator.validateOrderRequest(주문_요청)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않을 경우 Exception")
    public void createTableNotExistsException() {
        BDDMockito.given(menuRepository.countByIdIn(ArgumentMatchers.any(List.class))).willReturn(1L);
        BDDMockito.given(orderTableRepository.findById(주문_요청.getOrderTableId())).willReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> orderTableValidator.validateOrderRequest(주문_요청)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있을 경우 Exception")
    public void createEmptyTableException() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, true);

        BDDMockito.given(menuRepository.countByIdIn(ArgumentMatchers.any(List.class))).willReturn(1L);
        BDDMockito.given(orderTableRepository.findById(주문_요청.getOrderTableId())).willReturn(Optional.of(주문_테이블));

        Assertions.assertThatThrownBy(() -> orderTableValidator.validateOrderRequest(주문_요청)).isInstanceOf(
                IllegalArgumentException.class);
    }
}
