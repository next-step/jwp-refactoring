package kitchenpos.tablegroup.domain;

import static kitchenpos.ordertable.application.fixture.OrderTableFixture.한명_주문테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.exception.InvalidParameterException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TableGroupValidator 클래스")
@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @Test
    @DisplayName("`단체 지정`은 `주문 테이블`이 다른 `단체 지정`에 속해있지 않아야 등록 할 수 있다.")
    void 다른_단체지정에_포함된_주문테이블_단체지정_실패() {
        // given
        List<OrderTable> 손님있는_테이블 = Arrays.asList(한명_주문테이블(), 한명_주문테이블());

        // when
        when(orderTableRepository.findAllById(any())).thenReturn(손님있는_테이블);
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupValidator.validateGroupExist(
            Arrays.asList(1L, 2L));

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("단체지정에 속한 주문목록에서 계산완료가 아닌 주문상가 있을경우 에러 발생")
    void 단체지정에_속한_주문_계산완료_검증() {
        // given
        List<OrderTable> 손님있는_테이블 = Arrays.asList(한명_주문테이블(), 한명_주문테이블());

        // when
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(손님있는_테이블);
        when(orderRepository.existsAllByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(
            true);

        ThrowableAssert.ThrowingCallable actual = () -> tableGroupValidator.validateCompletedOrders(
            TableGroup.of());

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }
}
