package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kitchenpos.table.domain.fixture.OrderTableFixtureFactory;
import kitchenpos.table.exception.CannotChangeEmptyState;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChangeEmptyDomainServiceTest {

    @Mock
    private TableOrderStatusChecker tableOrderStatusChecker;

    @Mock
    private OrderTableRepository orderTableRepository;

    private ChangeEmptyDomainService changeEmptyDomainService;

    @BeforeEach
    void setUp() {
        changeEmptyDomainService = new ChangeEmptyDomainService(tableOrderStatusChecker, orderTableRepository);
    }

    @Test
    @DisplayName("주문의 상태가 계산완료가 아닌 경우 공석여부 변경 시도시 실패")
    void 테이블_공석상태변경_주문이_조리_식사상태인경우() {
        Long orderTableId = 1L;
        OrderTable notEmptyTable = OrderTableFixtureFactory.createNotEmptyOrderTable(3);
        when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(notEmptyTable));
        when(tableOrderStatusChecker.isBeforeBillingStatus(orderTableId)).thenReturn(true);

        assertThatThrownBy(() -> changeEmptyDomainService.changeEmpty(orderTableId, true))
                .isInstanceOf(CannotChangeEmptyState.class);
    }
}
