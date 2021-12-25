package kitchenpos.tableGroup.application;

import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.tableGroup.dto.OrderTableIdRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class TableValidatorTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @DisplayName("단체 지정 할 때 주문 테이블이 2개 이상이여야 한다.")
    @Test
    void createOrderTableSizeError() {
        OrderTableIdRequest 테이블요청 = new OrderTableIdRequest(1L);

        assertThatThrownBy(
                () -> tableGroupValidator.getOrderTable(Arrays.asList(테이블요청))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
