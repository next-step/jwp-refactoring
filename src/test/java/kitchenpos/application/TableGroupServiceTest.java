package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.CreateTableGroupException;
import kitchenpos.exception.DontUnGroupException;
import org.assertj.core.util.Lists;
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
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderTableService orderTableService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 단체_1;
    private OrderTable 주문_1_테이블;
    private OrderTable 주문_2_테이블;
    private OrderTable 주문_테이블_10명;

    @BeforeEach
    void setUp() {
        주문_1_테이블 = OrderTableFixtureFactory.createWithGuest(true, 2);
        주문_2_테이블 = OrderTableFixtureFactory.createWithGuest(true, 2);
        주문_테이블_10명 = OrderTableFixtureFactory.createWithGuest(false, 10);
        단체_1 = TableGroupFixtureFactory.create(1L, Lists.newArrayList(주문_1_테이블, 주문_2_테이블));
    }

    @DisplayName("단체를 지정할 수 있다.")
    @Test
    void create01() {
        // given
        TableGroupRequest request = TableGroupRequest.from(Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId()));
        given(orderTableService.findOrderTables(
                        Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId()))
        ).willReturn(Lists.newArrayList(주문_1_테이블, 주문_2_테이블));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체_1);

        // when
        TableGroupResponse response = tableGroupService.create(request);

        // then
        assertThat(response).isEqualTo(TableGroupResponse.from(단체_1));
    }

    @DisplayName("주문 테이블이 비어있으면 테이블을 단체로 지정할 수 없다.")
    @Test
    void create02() {
        // given
        TableGroupRequest request = TableGroupRequest.from(Collections.emptyList());

        // when & then
        assertThatExceptionOfType(CreateTableGroupException.class)
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("주문 테이블이 1개이면 테이블을 단체로 지정할 수 없다.")
    @Test
    void create03() {
        // given
        TableGroupRequest request = TableGroupRequest.from(Lists.newArrayList(주문_1_테이블.getId()));

        // when & then
        assertThatExceptionOfType(CreateTableGroupException.class)
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("단체에 속하는 주문 테이블이 존재하지 않으면 단체로 지정할 수 없다.")
    @Test
    void create04() {
        // given
        TableGroupRequest request = TableGroupRequest.from(Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId()));

        given(orderTableService.findOrderTables(Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId())))
                .willReturn(Collections.emptyList());

        // when & then
        assertThatExceptionOfType(CreateTableGroupException.class)
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("단체에 속하는 주문 테이블이 빈 테이블이 아니면 단체로 지정할 수 없다.")
    @Test
    void create05() {
        // given
        TableGroupRequest request = TableGroupRequest.from(Lists.newArrayList(주문_테이블_10명.getId()));

        // when & then
        assertThatExceptionOfType(CreateTableGroupException.class)
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("단체에 속하는 주문 테이블이 이미 테이블 그룹에 속해있으면 단체로 지정할 수 없다.")
    @Test
    void create06() {
        // given
        TableGroupRequest request = TableGroupRequest.from(Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId()));

        주문_1_테이블.mappedByTableGroup(단체_1);

        // when & then
        assertThatExceptionOfType(CreateTableGroupException.class)
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("단체를 해제할 수 있다.")
    @Test
    void change01() {
        // given
        주문_1_테이블.mappedByTableGroup(단체_1);
        주문_2_테이블.mappedByTableGroup(단체_1);

        given(tableGroupRepository.findById(단체_1.getId())).willReturn(Optional.ofNullable(단체_1));

        // when
        tableGroupService.ungroup(단체_1.getId());

        // then
        assertAll(
                () -> assertThat(주문_1_테이블.getTableGroup()).isNull(),
                () -> assertThat(주문_2_테이블.getTableGroup()).isNull()
        );
    }

    @DisplayName("주문 테이블의 주문 상태가 COOKING 이거나 MEAL 인 경우 단체를 해제할 수 없다.")
    @Test
    void change02() {
        // given
        주문_1_테이블.mappedByTableGroup(단체_1);
        주문_2_테이블.mappedByTableGroup(단체_1);

        given(orderService.isExistDontUnGroupState(anyList())).willReturn(true);
        given(tableGroupRepository.findById(단체_1.getId())).willReturn(Optional.ofNullable(단체_1));

        // when & then
        assertThatExceptionOfType(DontUnGroupException.class)
                .isThrownBy(() -> tableGroupService.ungroup(단체_1.getId())
        );
    }
}