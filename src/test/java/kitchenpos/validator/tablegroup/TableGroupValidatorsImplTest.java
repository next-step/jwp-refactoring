package kitchenpos.validator.tablegroup;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.validator.tablegroup.impl.AlreadyGroupedTableGroupValidator;
import kitchenpos.validator.tablegroup.impl.OrderStatusTableGroupValidator;
import kitchenpos.validator.tablegroup.impl.OrderTableEmptyValidator;
import kitchenpos.validator.tablegroup.impl.OrderTablesSizeValidator;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorsImplTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    private TableGroupValidatorsImpl tableGroupValidatorImpl;

    @BeforeEach
    void setUp() {
        List<TableGroupValidator> tableGroupValidators = Arrays
                .asList(new OrderTableEmptyValidator(), new AlreadyGroupedTableGroupValidator(),
                        new OrderTablesSizeValidator(),
                        new OrderStatusTableGroupValidator(orderRepository));

        tableGroupValidatorImpl = new TableGroupValidatorsImpl(orderTableRepository, tableGroupValidators);
    }

    @Test
    void 등록_된_주문_테이블에_대해서만_단체_지정이_가능하다() {
        given(orderTableRepository.findAllByIdIn(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 등록되지_않은_주문_테이블_단체_지정할_경우 = () -> tableGroupValidatorImpl
                .validateCreation(1L, eventPublisher, null);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문_테이블_단체_지정할_경우);
    }

    @Test
    void 비어있지_않은_주문_테이블은_단체_지정을_할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Optional.of(Collections.singletonList(orderTable)));

        ThrowingCallable 비어있지_않은_주문_테이블_단체_지정할_경우 = () -> tableGroupValidatorImpl
                .validateCreation(1L, eventPublisher, null);

        assertThatIllegalArgumentException().isThrownBy(비어있지_않은_주문_테이블_단체_지정할_경우);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블은_단체_지정을_할_수_없다() {
        OrderTable orderTable = new OrderTable(1, true);
        orderTable.changeTableGroupId(1L);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Optional.of(Collections.singletonList(orderTable)));

        ThrowingCallable 이미_단체_지정이_된_주문_테이블_단체_지정할_경우 = () -> tableGroupValidatorImpl
                .validateCreation(1L, eventPublisher, null);

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_주문_테이블_단체_지정할_경우);
    }

    @Test
    void 최소_2개_이상의_주문_테이블들에_대해서만_단체_지정_가능하다() {
        OrderTable orderTable = new OrderTable(1, true);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Optional.of(Collections.singletonList(orderTable)));

        ThrowingCallable 주문_테이블을_1개만_지정한_경우 = () -> tableGroupValidatorImpl.validateCreation(1L, eventPublisher, null);

        assertThatIllegalArgumentException().isThrownBy(주문_테이블을_1개만_지정한_경우);
    }
}
