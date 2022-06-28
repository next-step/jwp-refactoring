package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFactory.createOrderTable;
import static kitchenpos.fixture.TableGroupFactory.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private TableGroupRequest 단체지정요청;

    @BeforeEach
    void setUp() {
        주문테이블1 = createOrderTable(1L, null, 5, true);
        주문테이블2 = createOrderTable(2L, null, 5, true);
        단체지정요청 = createTableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));
    }

    @Test
    void 단체_지정_테이블_개수_2개_미만_예외() {
        // given
        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L))).willReturn(Arrays.asList(주문테이블1));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(any(TableGroup.class));

        // when, then
        assertThatThrownBy(
                () -> tableGroupService.create(
                        createTableGroupRequest(Arrays.asList(1L))
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_존재하지_않는_테이블_예외() {
        // given
        given(orderTableRepository.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).willReturn(
                Arrays.asList(주문테이블1));

        // when, then
        assertThatThrownBy(
                () -> tableGroupService.create(단체지정요청)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_빈_테이블_아님_예외() {
        // given
        OrderTable 빈테이블 = createOrderTable(3L, null, 5, false);
        given(orderTableRepository.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 빈테이블.getId()))).willReturn(
                Arrays.asList(주문테이블1, 빈테이블));

        // when, then
        assertThatThrownBy(
                () -> tableGroupService.create(createTableGroupRequest(Arrays.asList(주문테이블1.getId(), 빈테이블.getId())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_이미_단체_지정_테이블_예외() {
        // given
        주문테이블1.groupByTableGroupId(2L);
        given(orderTableRepository.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).willReturn(
                Arrays.asList(주문테이블1, 주문테이블2));

        // when, then
        assertThatThrownBy(
                () -> tableGroupService.create(단체지정요청)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정() {
        // given
        TableGroup 단체지정 = new TableGroup();
        given(orderTableRepository.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).willReturn(
                Arrays.asList(주문테이블1, 주문테이블2));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체지정);

        // when
        TableGroupResponse result = tableGroupService.create(단체지정요청);

        // then
        assertThat(result.getId()).isEqualTo(단체지정.getId());
    }

    @Test
    void 단체_지정_삭제_주문_상태_예외() {
        // given
        TableGroup 단체지정 = new TableGroup(1L, null, Arrays.asList(주문테이블1, 주문테이블2));
        given(tableGroupRepository.findById(단체지정.getId())).willReturn(Optional.of(단체지정));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        )).willReturn(true);

        // when, then
        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체지정.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_삭제() {
        // given
        TableGroup 단체지정 = new TableGroup(1L, null, Arrays.asList(주문테이블1, 주문테이블2));
        given(tableGroupRepository.findById(단체지정.getId())).willReturn(Optional.of(단체지정));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        )).willReturn(false);

        tableGroupService.ungroup(단체지정.getId());

        assertAll(
                () -> assertThat(주문테이블1.isGroupedByTableGroup()).isEqualTo(false),
                () -> assertThat(주문테이블1.isGroupedByTableGroup()).isEqualTo(false)
        );
    }
}
