package kitchenpos.order.application;

import kitchenpos.exception.OrderErrorMessage;
import kitchenpos.exception.OrderTableErrorMessage;
import kitchenpos.exception.TableGroupErrorMessage;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("단체 지정 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 우아한형제들_단체그룹;
    private OrderTable 첫번째_주문_테이블;
    private OrderTable 두번째_주문_테이블;
    private OrderTable 세번째_주문_테이블;
    private OrderTable 네번째_주문_테이블;
    private List<OrderTable> 주문_테이블_목록;

    @BeforeEach
    void setUp() {
        우아한형제들_단체그룹 = new TableGroup();

        첫번째_주문_테이블 = new OrderTable(4, true);
        두번째_주문_테이블 = new OrderTable(4, true);
        세번째_주문_테이블 = new OrderTable(2, true);
        네번째_주문_테이블 = new OrderTable(2, true);

        ReflectionTestUtils.setField(우아한형제들_단체그룹, "id", 1L);
        ReflectionTestUtils.setField(첫번째_주문_테이블, "id", 1L);
        ReflectionTestUtils.setField(두번째_주문_테이블, "id", 2L);
        ReflectionTestUtils.setField(세번째_주문_테이블, "id", 3L);
        ReflectionTestUtils.setField(네번째_주문_테이블, "id", 4L);

        주문_테이블_목록 = Arrays.asList(첫번째_주문_테이블, 두번째_주문_테이블);
        우아한형제들_단체그룹.group(주문_테이블_목록);
    }

    @DisplayName("주문 테이블들이 2개 이상 있어야 한다.")
    @Test
    void 주문_테이블들이_2개_이상_있어야_한다() {
        // given
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(첫번째_주문_테이블));

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(첫번째_주문_테이블.getId()));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(TableGroupErrorMessage.MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
    }

    @DisplayName("주문 테이블들은 모두 등록된 주문 테이블이어야 한다.")
    @Test
    void 주문_테이블들은_모두_등록된_주문_테이블이어야_한다() {
        // given
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(첫번째_주문_테이블));

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(첫번째_주문_테이블.getId(), 두번째_주문_테이블.getId()));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(OrderTableErrorMessage.NOT_FOUND_BY_ID.getMessage());
    }

    @DisplayName("주문 테이블들은 빈 테이블이어야 한다.")
    @Test
    void 주문_테이블들은_빈_테이블이어야_한다() {
        // given
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(첫번째_주문_테이블, 두번째_주문_테이블));

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(첫번째_주문_테이블.getId(), 두번째_주문_테이블.getId()));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(OrderTableErrorMessage.NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP.getMessage());
    }

    @DisplayName("이미 단체 지정된 주문 테이블은 단체 지정할 수 없다.")
    @Test
    void 이미_단체_지정된_주문_테이블은_단체_지정할_수_없다() {
        // given
        ReflectionTestUtils.setField(첫번째_주문_테이블, "empty", new OrderEmpty(true));
        ReflectionTestUtils.setField(두번째_주문_테이블, "empty", new OrderEmpty(true));

        when(orderTableRepository.findAllById(Arrays.asList(첫번째_주문_테이블.getId(), 두번째_주문_테이블.getId())))
                .thenReturn(Arrays.asList(첫번째_주문_테이블, 두번째_주문_테이블));

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(첫번째_주문_테이블.getId(), 두번째_주문_테이블.getId()));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(OrderTableErrorMessage.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
    }

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void 단체_지정을_등록한다() {
        // given
        when(orderTableRepository.findAllById(Arrays.asList(세번째_주문_테이블.getId(), 네번째_주문_테이블.getId())))
                .thenReturn(Arrays.asList(세번째_주문_테이블, 네번째_주문_테이블));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(우아한형제들_단체그룹);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(세번째_주문_테이블.getId(), 네번째_주문_테이블.getId()));

        // when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(tableGroupResponse).satisfies(response -> {
            assertEquals(response.getId(), 우아한형제들_단체그룹.getId());
            assertEquals(response.getOrderTables().size(), 우아한형제들_단체그룹.getOrderTables().size());
        });
    }

    @DisplayName("단체 내 주문 테이블들의 상태는 조리 중이거나 식사중이면 안된다.")
    @Test
    void 단체_내_주문_테이블들의_상태는_조리_중이거나_식사중이면_안된다() {
        // given
        when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(우아한형제들_단체그룹));
        when(orderRepository.findAllByOrderTableIds(anyList()))
                .thenReturn(Arrays.asList(new Order(첫번째_주문_테이블, OrderStatus.COMPLETION), new Order(두번째_주문_테이블, OrderStatus.COOKING)));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(우아한형제들_단체그룹.getId()))
                .withMessage(OrderErrorMessage.CANNOT_BE_CHANGED.getMessage());
    }

    @DisplayName("단체 지정을 삭제할 수 있다.")
    @Test
    void 단체_지정을_삭제할_수_있다() {
        // given
        when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(우아한형제들_단체그룹));
        when(orderRepository.findAllByOrderTableIds(anyList()))
                .thenReturn(Arrays.asList(new Order(첫번째_주문_테이블, OrderStatus.COMPLETION), new Order(두번째_주문_테이블, OrderStatus.COMPLETION)));
        // when
        tableGroupService.ungroup(우아한형제들_단체그룹.getId());

        // then
        assertTrue(주문_테이블_목록.stream().allMatch(orderTable -> orderTable.getTableGroup() == null));
    }
}
