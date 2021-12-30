package kitchenpos.tableGroup;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tableGroup.application.TableGroupService;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 관련 기능")
public class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private TableGroupRequest tableGroupRequest;
    OrderTable orderTable1;
    OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = 주문_테이블_생성(1L, 2, true);
        orderTable2 = 주문_테이블_생성(2L, 4, true);
        tableGroupRequest = 단체_지정_요청(Arrays.asList(new OrderTableRequest(orderTable1.getId()), new OrderTableRequest(orderTable2.getId())));
    }

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        tableGroup = mock(TableGroup.class);
        given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        TableGroupResponse createTableGroup = tableGroupService.create(tableGroupRequest);
        assertThat(createTableGroup).isNotNull();
    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 테이블 갯수가 2보다 작으면 실패한다.")
    void createWithUnderTwoOrderTable() {
        given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(orderTable1));
        TableGroupRequest tableGroupRequest = 단체_지정_요청(Arrays.asList(new OrderTableRequest(orderTable1.getId())));

        단체_지정_실패(tableGroupRequest, "주문 테이블을 2개 이상");
    }

    @Test
    @DisplayName("테이블 그룹으로 등록하려는 테이블이 DB에 없으면 실패한다.")
    void createWithDifferentOrderTable() {
        given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(orderTable1));

        단체_지정_실패(tableGroupRequest, "주문 테이블 정보가 존재하지 않습니다");
    }

    @Test
    @DisplayName("테이블 그룹으로 등록하려는 테이블이 비어있지 않거나 다른 그룹에 등록되어 있으면 실패한다.")
    void createWithNotEmptyOrderTableOrNonNullTableGroupId() {
        tableGroup = mock(TableGroup.class);
        when(tableGroup.getId()).thenReturn(1L);
        orderTable1.group(1L);
        OrderTable orderTable3 = 주문_테이블_생성(3L, 2, false);
        given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(orderTable1, orderTable3));
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        단체_지정_실패(tableGroupRequest, "비어있지 않거나 이미 단체 지정된 테이블");
    }

    @Test
    @DisplayName("단체 지정을 해제한다.")
    void ungroup() {
        tableGroup = mock(TableGroup.class);
        when(tableGroup.getId()).thenReturn(1L);
        orderTable1.group(1L);
        orderTable2.group(1L);
        given(tableGroupRepository.findById(any())).willReturn(Optional.ofNullable(tableGroup));

        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);
        tableGroupService.ungroup(tableGroup.getId());

        assertThat(orderTable1.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("단체 지정을 해제할 때 식사중이거나 조리중인 테이블이 있으면 실패한다.")
    void ungroupWithCookingOrMealOrderTable() {
        tableGroup = mock(TableGroup.class);
        when(tableGroup.getId()).thenReturn(1L);
        orderTable1.group(1L);
        orderTable2.group(1L);
        given(tableGroupRepository.findById(any())).willReturn(Optional.ofNullable(tableGroup));

        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.ungroup(tableGroup.getId());
        }).withMessageContaining("테이블이 식사중이거나 조리중이면 단체 지정을 해제할 수 없습니다.");
    }

    private OrderTable 주문_테이블_생성(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    private TableGroupRequest 단체_지정_요청(List<OrderTableRequest> orderTables) {
        return new TableGroupRequest(orderTables);
    }

    private void 단체_지정_실패(TableGroupRequest tableGroupRequest, String message) {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).withMessageContaining(message);
    }
}
