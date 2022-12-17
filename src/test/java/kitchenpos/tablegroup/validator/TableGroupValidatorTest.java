package kitchenpos.tablegroup.validator;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    private TableGroupValidator tableGroupValidator;

    @BeforeEach
    void setUp() {
        tableGroupValidator = new TableGroupValidator(orderTableRepository, orderRepository);
    }

    @Test
    void 등록_된_주문_테이블에_대해서만_단체_지정이_가능하다() {
        given(orderTableRepository.findAllByIdIn(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 등록되지_않은_주문_테이블_단체_지정할_경우 = () -> tableGroupValidator
                .validateCreation(1L, eventPublisher, null);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문_테이블_단체_지정할_경우);
    }

    @Test
    void 비어있지_않은_주문_테이블은_단체_지정을_할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Optional.of(Collections.singletonList(orderTable)));

        ThrowingCallable 비어있지_않은_주문_테이블_단체_지정할_경우 = () -> tableGroupValidator
                .validateCreation(1L, eventPublisher, null);

        assertThatIllegalArgumentException().isThrownBy(비어있지_않은_주문_테이블_단체_지정할_경우);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블은_단체_지정을_할_수_없다() {
        OrderTable orderTable = new OrderTable(1, true);
        orderTable.changeTableGroupId(1L);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Optional.of(Collections.singletonList(orderTable)));

        ThrowingCallable 이미_단체_지정이_된_주문_테이블_단체_지정할_경우 = () -> tableGroupValidator
                .validateCreation(1L, eventPublisher, null);

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_주문_테이블_단체_지정할_경우);
    }

    @Test
    void 최소_2개_이상의_주문_테이블들에_대해서만_단체_지정_가능하다() {
        OrderTable orderTable = new OrderTable(1, true);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Optional.of(Collections.singletonList(orderTable)));

        ThrowingCallable 주문_테이블을_1개만_지정한_경우 = () -> tableGroupValidator.validateCreation(1L, eventPublisher, null);

        assertThatIllegalArgumentException().isThrownBy(주문_테이블을_1개만_지정한_경우);
    }
}
