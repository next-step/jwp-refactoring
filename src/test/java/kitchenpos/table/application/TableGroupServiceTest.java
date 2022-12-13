package kitchenpos.table.application;

import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;


@DisplayName("테이블그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;


    private TableGroup 테이블그룹;
    private TableGroupRequest 테이블그룹_요청;
    private OrderTable 주문테이블_4명;
    private OrderTable 주문테이블_6명;

    @BeforeEach
    void set_up() {
        주문테이블_4명 = OrderTableFixture.create(null, 34, true);
        주문테이블_6명 = OrderTableFixture.create(null, 46, true);
        테이블그룹 = TableGroup.of();

        OrderTableRequest 주문테이블_4명_요청 = new OrderTableRequest(1L, 6, true);
        OrderTableRequest 주문테이블_6명_요청 = new OrderTableRequest(2L, 8, true);
        List<OrderTableRequest> 주문테이블_요청들 = Arrays.asList(주문테이블_4명_요청, 주문테이블_6명_요청);
        테이블그룹_요청 = new TableGroupRequest(주문테이블_요청들);

    }

    @DisplayName("테이블 그룹을 등록할 수 있다.")
    @Test
    void create() {
        // given
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(주문테이블_4명, 주문테이블_6명));
        when(tableGroupRepository.save(any())).thenReturn(테이블그룹);

        // when
        TableGroupResponse 테이블_그룹_등록 = tableGroupService.create(테이블그룹_요청);

        // then
        assertThat(테이블_그룹_등록.getOrderTables()).hasSize(테이블그룹.getOrderTables().size());
    }


    @DisplayName("주문 테이블 리스트가 저장된 주문 테이블 리스트의 개수와 일치하지 않으면 테이블 그룹을 등록할 수 없다.")
    @Test
    void create_error_not_match_size() {
        // given
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(주문테이블_4명));

        // when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있지 않거나 주문 테이블의 다른 그룹 테이블 정보가 있다면 테이블 그룹을 등록할 수 없다.")
    @Test
    void create_error_using_table() {
        // given
        OrderTable 주문테이블_그룹있음 = OrderTableFixture.create(null, 6, false);
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(주문테이블_4명, 주문테이블_그룹있음));

        // when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록을 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        OrderTable 주문테이블_그룹있음 = OrderTableFixture.create(null, 99, true);
        테이블그룹 = new TableGroup(Arrays.asList(주문테이블_4명, 주문테이블_6명, 주문테이블_그룹있음));

        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(테이블그룹));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);

        // when
        tableGroupService.ungroup(테이블그룹.getId());

        // then
        assertAll(
                () -> assertThat(주문테이블_그룹있음.getTableGroup()).isNull(),
                () -> assertThat(테이블그룹.getOrderTables()).hasSize(3)
        );
    }

    @DisplayName("테이블 그룹의 주문 테이블 리스트 중에 주문상태가 조리 또는 식사 중인 경우 테이블 그룹 등록을 해제할 수 없다.")
    @Test
    void ungroup_error_table_meal() {
        // given
        테이블그룹 = new TableGroup(Arrays.asList(주문테이블_4명, 주문테이블_6명));
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(테이블그룹));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문테이블_4명.getId(), 주문테이블_6명.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(true);

        // when && then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
