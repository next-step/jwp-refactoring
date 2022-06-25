package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private OrderTable 주문_테이블1;
    private OrderTable 주문_테이블2;

    private TableGroup 단체;

    @BeforeEach
    void before() {
        주문_테이블1 = OrderTableFixtureFactory.createByGuestNumber(1L, 2, true);
        주문_테이블2 = OrderTableFixtureFactory.createByGuestNumber(2L, 3, true);
        단체 = TableGroupFixtureFactory.create(1L);
    }

    @Test
    @DisplayName("주문 테이블들이 시스템에 등록 되어 있지 않으면 테이블 그룹은 지정 할 수 없다.")
    void createFailWithOrderTableNotExistTest() {

        //given
        TableGroup tableGroup = new TableGroup(1L, Arrays.asList(주문_테이블1, 주문_테이블2));
        given(orderTableRepository.findAllByIdIn(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId()))).willReturn(
                Arrays.asList(주문_테이블1));

        //when & then
        assertThatThrownBy(
                () -> tableGroupService.create(TableGroupRequest.from(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 지정 할 수 있다.")
    void createTest() {
        //given
        given(orderTableRepository.findAllByIdIn(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId()))).willReturn(
                Arrays.asList(주문_테이블1, 주문_테이블1));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체);

        //when
        TableGroupResponse response = tableGroupService.create(TableGroupRequest.from(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId())));
        //then
        assertThat(response.getId()).isEqualTo(TableGroupResponse.of(단체).getId());
    }


    @Test
    @DisplayName("주문 상태가 조리중(COOKING), 식사중(MEAL)인 경우에는 해제 할 수 없다.")
    void ungroupFailWithStatusTest() {
        //given
        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(단체));
        given(orderTableRepository.findAllByTableGroupId(단체.getId())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //when & then
        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("지정된 테이블 그룹을 해제 할 수 있다.")
    void ungroupTest() {
        //given
        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(단체));
        given(orderTableRepository.findAllByTableGroupId(단체.getId())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        //when
        tableGroupService.ungroup(단체.getId());

        //then
        assertThat(단체.getOrderTables()).isEmpty();
    }
}
