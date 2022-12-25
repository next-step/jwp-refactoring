package kitchenpos.application.table.application;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
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

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@DisplayName("테이블그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    private TableGroup 단체1;
    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;
    private OrderTable 테이블_GUEST3_NOT_EMPTY;
    private OrderTable 테이블_TABLEGROUP;

    private TableGroup 테이블그룹;
    private OrderTable 주문테이블_4명;
    private OrderTable 주문테이블_6명;

    private TableGroupRequest 테이블그룹_요청;

    @BeforeEach
    void setUp() {
        테이블1 = new OrderTable(0, true);
        테이블2 = new OrderTable(0, true);
        테이블3 = new OrderTable(0, true);
        테이블_GUEST3_NOT_EMPTY = new OrderTable(3, false);
        테이블_TABLEGROUP = new OrderTable(3, false);

        주문테이블_4명 = new OrderTable(4, true);
        주문테이블_6명 = new OrderTable(6, true);

        OrderTableRequest 주문테이블_4명_요청 = new OrderTableRequest(1L, 6, true);
        OrderTableRequest 주문테이블_6명_요청 = new OrderTableRequest(2L, 8, true);
        List<OrderTableRequest> 주문테이블_요청들 = Arrays.asList(주문테이블_4명_요청, 주문테이블_6명_요청);
        테이블그룹_요청 = new TableGroupRequest(주문테이블_요청들);
    }

    @Test
    @DisplayName("단체을 등록한다.")
    void createTableGroup() {
        // given
        OrderTable 주문테이블_8명 = new OrderTable(8, true);
        OrderTable 주문테이블_10명 = new OrderTable(10, true);
        테이블그룹 = new TableGroup(Arrays.asList(주문테이블_8명, 주문테이블_10명));
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(주문테이블_4명, 주문테이블_6명));
        when(tableGroupRepository.save(any())).thenReturn(테이블그룹);

        // when
        TableGroupResponse 테이블_그룹_등록 = tableGroupService.create(테이블그룹_요청);

        // then
        assertThat(테이블_그룹_등록.getOrderTables()).hasSize(테이블그룹.getOrderTables().size());
    }

    @Test
    @DisplayName("단체 내 주문테이블이 비어 있지 않으면 오류 발생한다.")
    void notEmptyOrderTableException() {
        // given
        given(orderTableRepository.findAllById(anyList())).willReturn(
            Arrays.asList(테이블1, 테이블_GUEST3_NOT_EMPTY));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(테이블그룹_요청));
    }

    @Test
    @DisplayName("다른 단체에 지정된 주문테이블이 있으면 등록 시, 오류 발생한다.")
    void alreadyTableGroupException() {
        // given
        given(orderTableRepository.findAllById(anyList())).willReturn(
            Arrays.asList(테이블1, 테이블_TABLEGROUP));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(테이블그룹_요청));
    }

    @Test
    @DisplayName("단체을 삭제한다.")
    void unTableGroup() {
        // given
        OrderTable 주문테이블_그룹있음 = new OrderTable(99, true);
        테이블그룹 = new TableGroup(Arrays.asList(주문테이블_4명, 주문테이블_6명, 주문테이블_그룹있음));

        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(테이블그룹));

        // when
        tableGroupService.ungroup(테이블그룹.getId());

        // then
        assertAll(
            () -> assertThat(주문테이블_그룹있음.getTableGroup()).isNull(),
            () -> assertThat(테이블그룹.getOrderTables()).hasSize(3)
        );
    }

    @Test
    @DisplayName("단체 삭제 시, 주문 테이블의 상태가 주문 중 / 식사중 이면 오류 발생한다.")
    void unTableGroupException() {
        // given
        테이블그룹 = new TableGroup(Arrays.asList(주문테이블_4명, 주문테이블_6명));
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(테이블그룹));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        // when && then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

