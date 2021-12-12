package kitchenpos.ordertable.application;

import kitchenpos.ServiceTest;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@DisplayName("테이블 서비스 Mock 테스트")
class OrderTableServiceMockTest extends ServiceTest {

    @MockBean
    private OrderDao orderDao;

    @Test
    @DisplayName("올바르지 않은 주문으로 테이블의 상태를 변경하면 예외를 발생한다.")
    void changeEmptyThrowException() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_저장(false);
        OrderTableRequest orderTableRequest = new OrderTableRequest(true);

        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTableService.changeEmpty(savedOrderTableResponse.getId(), orderTableRequest));
    }
}
