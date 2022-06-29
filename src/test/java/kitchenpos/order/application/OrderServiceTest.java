package kitchenpos.order.application;

import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    TableRepository tableRepository;

    @InjectMocks
    OrderService orderService;

    @DisplayName("빈 테이블이면 주문 생성에 실패한다.")
    @Test
    void 생성_예외_빈_테이블() {
        // given
        doReturn(new OrderTable(null, 0, true)).when(tableRepository).getById(any(Long.class));

        // when, then
        assertThatThrownBy(() -> orderService.create(new OrderRequest(1L, Arrays.asList(new OrderLineItemRequest(1L, 1)))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 주문을 등록할 수 없습니다.");
    }
}
