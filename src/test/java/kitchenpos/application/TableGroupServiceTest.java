package kitchenpos.application;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.domain.OrderTableTest.주문_테이블_생성;
import static kitchenpos.domain.TableGroupTest.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹 생성 시 주문 테이블이 1개 이하면 예외가 발생해야 한다")
    @Test
    void createTableGroupByInsufficientOrderTableCountTest() {
        // given
        TableGroup 테이블_그룹 = 테이블_그룹_생성(Collections.emptyList());

        // then
        테이블_그룹_생성_실패(() -> tableGroupService.create(테이블_그룹));
    }

    @DisplayName("테이블 그룹의 주문 테이블의 수가 저장된 주문 테이블의 수와 일치하지 않으면 예외가 발생해야 한다")
    @Test
    void createTableGroupByNotMatchOrderTableCountTest() {
        // given
        List<OrderTable> 주문_테이블_목록 = Arrays.asList(
                주문_테이블_생성(null, 0, false),
                주문_테이블_생성(null, 0, false),
                주문_테이블_생성(null, 0, false),
                주문_테이블_생성(null, 0, false)
        );
        TableGroup 테이블_그룹 = 테이블_그룹_생성(주문_테이블_목록);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Collections.emptyList());

        // then
        테이블_그룹_생성_실패(() -> tableGroupService.create(테이블_그룹));
    }

    @DisplayName("빈 주문 테이블이 아니거나 이미 테이블 그룹이 설정된 주문 테이블로 생성하면 예외가 발생해야 한다")
    @Test
    void createTableGroupByNotEmptyOrAlreadyOrderedTableTest() {
        // given
        List<OrderTable> 이미_주문_된_테이블_리스트 = Arrays.asList(
                주문_테이블_생성(0L, 0, true),
                주문_테이블_생성(0L, 0, true),
                주문_테이블_생성(0L, 0, true)
        );
        List<OrderTable> 비어_있지_않은_테이블_리스트 = Arrays.asList(
                주문_테이블_생성(0L, 0, false),
                주문_테이블_생성(0L, 0, false),
                주문_테이블_생성(0L, 0, false)
        );
        TableGroup 이미_주문된_테이블_그룹 = 테이블_그룹_생성(이미_주문_된_테이블_리스트);
        TableGroup 비어_있지_않은_테이블_그룹 = 테이블_그룹_생성(비어_있지_않은_테이블_리스트);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(이미_주문_된_테이블_리스트);

        테이블_그룹_생성_실패(() -> tableGroupService.create(이미_주문된_테이블_그룹));
        테이블_그룹_생성_실패(() -> tableGroupService.create(비어_있지_않은_테이블_그룹));
    }

    @DisplayName("정상 상태의 테이블 그룹을 저장하면 정상 저장되어야 한다")
    @Test
    void createTableGroupTest() {
        // given
        List<OrderTable> 주문_테이블_목록 = Arrays.asList(
                주문_테이블_생성(null, 0, true),
                주문_테이블_생성(null, 0, true),
                주문_테이블_생성(null, 0, true),
                주문_테이블_생성(null, 0, true)
        );
        TableGroup 테이블_그룹 = 테이블_그룹_생성(주문_테이블_목록);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(주문_테이블_목록);
        when(tableGroupRepository.save(테이블_그룹)).thenReturn(테이블_그룹);
        when(orderTableRepository.save(any())).then(it -> it.getArgument(0));

        // when
        TableGroup 테이블_그룹_생성_결과 = tableGroupService.create(테이블_그룹);

        // then
        테이블_그룹_정상_생성됨(테이블_그룹_생성_결과, 테이블_그룹);
    }

    @DisplayName("주문 테이블의 상태가 요리중 또는 식사중인 테이블 그룹을 해제하면 예외가 발생해야 한다")
    @Test
    void ungroupByCookingOrMealStatusTableGroupTest() {
        // given
        when(orderTableRepository.findAllByTableGroupId(any()))
                .thenReturn(Collections.singletonList(주문_테이블_생성(0L, 10, false)));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        // then
        테이블_그룹_해제_실패(() -> tableGroupService.ungroup(0L));
    }

    @DisplayName("정상 상태의 주문 테이블의 테이블 그룹을 해제하면 정상 동작해야 한다")
    @Test
    void ungroupTest() {
        // given
        List<OrderTable> 주문_테이블_리스트 = Arrays.asList(
                주문_테이블_생성(0L, 1, false),
                주문_테이블_생성(0L, 2, false),
                주문_테이블_생성(0L, 3, false)
        );
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(주문_테이블_리스트);
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        // when
        tableGroupService.ungroup(0L);

        // then
        테이블_그룹_해제_성공(주문_테이블_리스트);
    }

    void 테이블_그룹_생성_실패(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 테이블_그룹_정상_생성됨(TableGroup source, TableGroup target) {
        assertThat(source.getOrderTables()).isEqualTo(target.getOrderTables());
    }

    void 테이블_그룹_해제_실패(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 테이블_그룹_해제_성공(List<OrderTable> source) {
        for (OrderTable orderTable : source) {
            assertThat(orderTable.getTableGroupId()).isNull();
        }
    }
}
