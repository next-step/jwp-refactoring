package kitchenpos.application.query;

import kitchenpos.application.command.OrderQueryService;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.dto.response.OrderViewResponse;
import kitchenpos.fixture.CleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.fixture.OrderFixture.결제완료_음식_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderQueryServiceTest {
    @Mock
    private OrderRepository orderRepository;

    private OrderQueryService orderQueryService;

    @BeforeEach
    void setUp() {
        CleanUp.cleanUp();

        orderQueryService = new OrderQueryService(orderRepository);
    }

    @Test
    @DisplayName("list - 정상적인 주문 목록 조회")
    void 정상적인_주문_목록_조회() {
        // when
        when(orderRepository.findAll()).thenReturn(Arrays.asList(결제완료_음식_2));

        OrderViewResponse savedOrder = orderQueryService.list().get(0);

        // then
        assertThat(savedOrder).isEqualTo(OrderViewResponse.of(결제완료_음식_2));

        verify(orderRepository, VerificationModeFactory.times(1)).findAll();
    }
}