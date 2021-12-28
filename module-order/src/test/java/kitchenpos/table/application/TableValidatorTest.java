package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.fixture.TableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 유효성 검증 테스트")
class TableValidatorTest {

    @InjectMocks
    private TableValidator tableValidator;

    @Mock
    private OrderRepository orderRepository;

    private OrderTable 비어있는_테이블;
    private OrderTable 비어있지_않은_테이블;
    private OrderTable 테이블_그룹에_속해있는_테이블;

    @BeforeEach
    public void setUp() {
        비어있는_테이블 = TableFixture.create(1L, null, 0, true);
        비어있지_않은_테이블 = TableFixture.create(4L, null, 0, false);
        테이블_그룹에_속해있는_테이블 = TableFixture.create(2L, 1L, 0, true);
    }

    @DisplayName("테이블 빈 자리 수정 유효성 검증 성공 테스트")
    @Test
    void validateChangeEmpty_success() {
        // given
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(비어있지_않은_테이블.getId(), orderStatuses)).willReturn(false);

        // when & then
        assertThatNoException()
                .isThrownBy(() -> tableValidator.validateChangeEmpty(비어있지_않은_테이블));
    }

    @DisplayName("테이블 빈 자리 수정 유효성 검증 실패 테스트 - 테이블 그룹에 속해 있음")
    @Test
    void validateChangeEmpty_failure_existTableGroup() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableValidator.validateChangeEmpty(테이블_그룹에_속해있는_테이블));
    }

    @DisplayName("테이블 빈 자리 수정 유효성 검증 실패 테스트 - 주문 상태가 COOKING 또는 MEAL")
    @Test
    void validateChangeEmpty_failure_orderStatus_cooking() {
        // given
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(비어있지_않은_테이블.getId(), orderStatuses)).willReturn(true);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableValidator.validateChangeEmpty(비어있지_않은_테이블));
    }

    @DisplayName("테이블 손님 수 수정 유효성 검증 성공 테스트")
    @Test
    void validateChangeNumberOfGuests_success() {
        // when & then
        assertThatNoException()
                .isThrownBy(() -> tableValidator.validateChangeNumberOfGuests(비어있지_않은_테이블));
    }

    @DisplayName("테이블 손님 수 수정 유효성 검증 성공 테스트 - 테이블이 비어있음")
    @Test
    void validateChangeNumberOfGuests_failure_empty() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableValidator.validateChangeNumberOfGuests(비어있는_테이블));
    }
}
