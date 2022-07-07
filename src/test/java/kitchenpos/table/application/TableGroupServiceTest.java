package kitchenpos.table.application;

import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.testfixture.CommonTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableValidator tableValidator;
    @Mock
    private OrderTableStatusValidator orderTableStatusValidator;
    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 빈_테이블_1;
    private OrderTable 빈_테이블_2;
    private OrderTable 빈_테이블_3;
    private OrderTable 주문_테이블;
    private OrderTable 단체_테이블;

    @BeforeEach
    void setUp() {
        빈_테이블_1 = createOrderTable(1L, null, 0, true);
        빈_테이블_2 = createOrderTable(2L, null, 0, true);
        빈_테이블_3 = createOrderTable(3L, null, 0, true);
        List<OrderTable> orderTables = Arrays.asList(
                createOrderTable(6L, null, 4, true),
                createOrderTable(7L, null, 5, true)
        );
        주문_테이블 = createOrderTable(4L, null, 4, false);
        단체_테이블 = createOrderTable(5L, new TableGroup(orderTables), 0, true);
    }

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void create_success() {
        // given
        OrderTable new_빈_테이블_1 = new OrderTable(1L, null, 0, true);
        OrderTable new_빈_테이블_2 = new OrderTable(2L, null, 0, true);
        OrderTable new_빈_테이블_3 = new OrderTable(3L, null, 0, true);
        List<OrderTable> 빈_테이블_목록 = Arrays.asList(new_빈_테이블_1, new_빈_테이블_2, new_빈_테이블_3);
        TableGroupRequest 단체지정_테이블_요청 = createTableGroupRequest(Arrays.asList(빈_테이블_1.getId(), 빈_테이블_2.getId(), 빈_테이블_3.getId()));
        when(orderTableRepository.findAllByIdIn(Arrays.asList(빈_테이블_1.getId(), 빈_테이블_2.getId(), 빈_테이블_3.getId())))
                .thenReturn(빈_테이블_목록);
        given(tableGroupRepository.save(any())).willReturn(createTableGroup(Arrays.asList(빈_테이블_1, 빈_테이블_2, 빈_테이블_3)));

        // when
        TableGroupResponse saved = tableGroupService.create(단체지정_테이블_요청);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved.getOrderTables()).hasSize(3)
        );
    }

    @DisplayName("단체 지정 등록에 실패한다. (테이블 수가 2개 미만인 경우)")
    @Test
    void create_fail_invalidSize() {
        // given
        TableGroupRequest 단체지정_테이블_요청 = createTableGroupRequest(Collections.emptyList());

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_테이블_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록에 실패한다. (존재하지 않는 테이블이 있는 경우)")
    @Test
    void create_fail_empty() {
        // given
        TableGroupRequest 단체지정_테이블_요청 = createTableGroupRequest(Arrays.asList(빈_테이블_1.getId(), 빈_테이블_2.getId(), 빈_테이블_3.getId()));
        when(orderTableRepository.findAllByIdIn(Arrays.asList(빈_테이블_1.getId(), 빈_테이블_2.getId(), 빈_테이블_3.getId())))
                .thenReturn(Collections.emptyList());

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_테이블_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록에 실패한다. (빈 테이블이 아닌 테이블이 있는 경우)")
    @Test
    void create_fail_emptyTable() {
        // given
        TableGroupRequest 단체지정_테이블_요청 = createTableGroupRequest(Arrays.asList(주문_테이블.getId(), 빈_테이블_2.getId(), 빈_테이블_3.getId()));
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_테이블, 빈_테이블_2, 빈_테이블_3));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_테이블_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록에 실패한다. (이미 단체 지정된 테이블이 있는 경우)")
    @Test
    void create_fail_tableGroup() {
        // given
        TableGroupRequest 단체지정_테이블_요청 = createTableGroupRequest(Arrays.asList(단체_테이블.getId(), 빈_테이블_2.getId(), 빈_테이블_3.getId()));
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(단체_테이블, 빈_테이블_2, 빈_테이블_3));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_테이블_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void unGroup_success() {
        // given
        TableGroup 단체지정_테이블 = createTableGroup(Arrays.asList(빈_테이블_1, 빈_테이블_2, 빈_테이블_3));
        given(orderTableRepository.findAllByTableGroup(any())).willReturn(Arrays.asList(빈_테이블_1, 빈_테이블_2, 빈_테이블_3));
        given(tableGroupRepository.findById(단체지정_테이블.getId())).willReturn(Optional.of(단체지정_테이블));

        // when
        tableGroupService.ungroup(단체지정_테이블.getId());

        // then
        assertAll(
                () -> assertThat(빈_테이블_1.getTableGroupId()).isNull(),
                () -> assertThat(빈_테이블_2.getTableGroupId()).isNull(),
                () -> assertThat(빈_테이블_3.getTableGroupId()).isNull()
        );
    }

    @DisplayName("단체 지정 해제에 실패한다. (주문 상태가 '조리' 또는 '식사' 인 테이블이 있는 경우")
    @Test
    void unGroup_fail() {
        // given
        List<OrderTable> orderTables = Arrays.asList(빈_테이블_1, 빈_테이블_2, 빈_테이블_3);
        TableGroup 단체지정_테이블 = createTableGroup(orderTables);
        given(orderTableRepository.findAllByTableGroup(any())).willReturn(orderTables);
        given(tableGroupRepository.findById(단체지정_테이블.getId())).willReturn(Optional.of(단체지정_테이블));
        willThrow(new IllegalArgumentException()).given(orderTableStatusValidator).validateOrderTablesStatus(orderTables);

        // then
        assertThatThrownBy(() -> {
            tableGroupService.ungroup(단체지정_테이블.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
