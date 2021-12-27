package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 그룹 유효성 검증 테스트")
class TableGroupValidatorTest {

    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @Mock
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    private OrderTable 비어있는_테이블1;
    private OrderTable 비어있는_테이블2;
    private OrderTable 비어있는_테이블3;
    private OrderTable 비어있지_않은_테이블;
    private OrderTable 테이블_그룹에_속해있지_않은_테이블;
    private OrderTable 테이블_그룹에_속해있는_테이블1;
    private OrderTable 테이블_그룹에_속해있는_테이블2;

    @BeforeEach
    public void setUp() {
        비어있는_테이블1 = TableFixture.create(1L, null, 0, true);
        비어있는_테이블2 = TableFixture.create(2L, null, 0, true);
        비어있는_테이블3 = TableFixture.create(3L, null, 0, true);
        비어있지_않은_테이블 = TableFixture.create(4L, null, 0, false);
        테이블_그룹에_속해있지_않은_테이블 = TableFixture.create(1L, null, 0, true);
        테이블_그룹에_속해있는_테이블1 = TableFixture.create(1L, 1L, 0, true);
        테이블_그룹에_속해있는_테이블2 = TableFixture.create(2L, 1L, 0, true);
    }

    @DisplayName("테이블 그룹 유효성 검증 성공 테스트")
    @Test
    void validateCreate_success() {
        // given
        given(tableService.findAllByIdIn(anyList())).willReturn(Arrays.asList(비어있는_테이블1, 비어있는_테이블2));

        // when & then
        assertThatNoException()
                .isThrownBy(() -> tableGroupValidator.validateCreate(Arrays.asList(비어있는_테이블1.getId(), 비어있는_테이블2.getId())));
    }

    @DisplayName("테이블 그룹 유효성 검증 실패 테스트 - 주문 테이블이 비어있지 않음")
    @Test
    void validateCreate_failure_validateOrderTables_notEmpty() {
        // given
        given(tableService.findAllByIdIn(anyList())).willReturn(Arrays.asList(비어있는_테이블1, 비어있지_않은_테이블));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupValidator.validateCreate(Arrays.asList(비어있는_테이블1.getId(), 비어있지_않은_테이블.getId())));

    }

    @DisplayName("테이블 그룹 유효성 검증 실패 테스트 - 주문 테이블이 테이블 그룹에 속해 있음")
    @Test
    void validateCreate_failure_validateOrderTables_belongToGroupTable() {
        // given
        given(tableService.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_그룹에_속해있지_않은_테이블, 테이블_그룹에_속해있는_테이블1));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupValidator.validateCreate(Arrays.asList(테이블_그룹에_속해있지_않은_테이블.getId(), 테이블_그룹에_속해있는_테이블1.getId())));

    }

    @DisplayName("테이블 그룹 유효성 검증 실패 테스트 - 그룹화할 테이블이 2개 미만")
    @Test
    void validateCreate_failure_validateOrderTablesSize_minimumSize() {
        // given
        given(tableService.findAllByIdIn(anyList())).willReturn(Arrays.asList(비어있는_테이블1));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupValidator.validateCreate(Arrays.asList(비어있는_테이블1.getId())));

    }

    @DisplayName("테이블 그룹 유효성 검증 실패 테스트 - 그룹화할 주문 테이블 수 일치하지 않음")
    @Test
    void validateCreate_failure_validateOrderTablesSize_invalidSize() {
        // given
        given(tableService.findAllByIdIn(anyList())).willReturn(Arrays.asList(비어있는_테이블1, 비어있는_테이블2, 비어있는_테이블3));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupValidator.validateCreate(Arrays.asList(비어있는_테이블1.getId(), 비어있는_테이블2.getId())));

    }

    @DisplayName("테이블 그룹 해제 유효성 검증 성공 테스트")
    @Test
    void validateUngroup_success() {
        // given
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        given(tableService.findAllByTableGroupId(테이블_그룹에_속해있는_테이블1.getTableGroupId()))
                .willReturn(Arrays.asList(테이블_그룹에_속해있는_테이블1, 테이블_그룹에_속해있는_테이블2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(테이블_그룹에_속해있는_테이블1.getId(), 테이블_그룹에_속해있는_테이블2.getId()), orderStatuses)).willReturn(false);

        // when & then
        assertThatNoException()
                .isThrownBy(() -> tableGroupValidator.validateUngroup(테이블_그룹에_속해있는_테이블1.getTableGroupId()));
    }

    @DisplayName("테이블 그룹 해제 유효성 검증 실패 테스트 - 주문 상태가 COOKING 또는 MEAL")
    @Test
    void validateUngroup_failure_validateOrderStatus() {
        // given
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        given(tableService.findAllByTableGroupId(테이블_그룹에_속해있는_테이블1.getTableGroupId()))
                .willReturn(Arrays.asList(테이블_그룹에_속해있는_테이블1, 테이블_그룹에_속해있는_테이블2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(테이블_그룹에_속해있는_테이블1.getId(), 테이블_그룹에_속해있는_테이블2.getId()), orderStatuses)).willReturn(true);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupValidator.validateUngroup(테이블_그룹에_속해있는_테이블1.getTableGroupId()));
    }
}
