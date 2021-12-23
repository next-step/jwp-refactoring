package kitchenpos.tablegroup.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.fixture.TableFixture;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupDao;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.fixture.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private TableService tableService;

    @Mock
    private OrderService orderService;

    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private TableGroup 회사A_단체_테이블;
    private OrderTable 그룹에_속해있는_테이블1;
    private OrderTable 그룹에_속해있는_테이블2;
    private TableGroup 회사B_단체_테이블;
    private OrderTable 비어있지_않은_테이블1;
    private OrderTable 비어있지_않은_테이블2;

    @BeforeEach
    public void setUp() {
        테이블1 = TableFixture.create(1L, null, 0, true);
        테이블2 = TableFixture.create(2L, null, 0, true);
        회사A_단체_테이블 = TableGroupFixture.create(1L, LocalDateTime.now(), Arrays.asList(테이블1, 테이블2));
        비어있지_않은_테이블1 = TableFixture.create(2L, 2L, 0, false);
        비어있지_않은_테이블2 = TableFixture.create(2L, 2L, 0, false);
        그룹에_속해있는_테이블1 = TableFixture.create(1L, 2L, 0, true);
        그룹에_속해있는_테이블2 = TableFixture.create(2L, 2L, 0, true);
        회사B_단체_테이블 = TableGroupFixture.create(2L, LocalDateTime.now(), Arrays.asList(그룹에_속해있는_테이블1, 그룹에_속해있는_테이블2));
    }

    @DisplayName("테이블 그룹 생성 성공 테스트")
    @Test
    void create_success() {
        // given
        TableGroupRequest 요청_테이블_그룹 = TableGroupRequest.of(Arrays.asList(OrderTableIdRequest.of(테이블1.getId()), OrderTableIdRequest.of(테이블2.getId())));

        given(tableService.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블2));
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(회사A_단체_테이블);

        // when
        TableGroupResponse 생성된_테이블_그룹 = tableGroupService.create(요청_테이블_그룹);

        // then
        assertThat(생성된_테이블_그룹).isEqualTo(TableGroupResponse.of(회사A_단체_테이블));
    }

    @DisplayName("테이블 그룹 생성 실패 테스트 - 테이블 그룹이될 주문 테이블 수가 2개 미만")
    @Test
    void create_failure_invalidSize() {
        // given
        TableGroupRequest 요청_테이블_그룹 = TableGroupRequest.of(Arrays.asList(OrderTableIdRequest.of(테이블1.getId())));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(요청_테이블_그룹));
    }

    @DisplayName("테이블 그룹 생성 실패 테스트 - 테이블 그룹이 될 주문 테이블이 존재하지 않음")
    @Test
    void create_failure_notFoundOrderTable() {
        // given
        TableGroupRequest 요청_테이블_그룹 = TableGroupRequest.of(Arrays.asList(OrderTableIdRequest.of(테이블1.getId()), OrderTableIdRequest.of(테이블2.getId())));

        given(tableService.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(요청_테이블_그룹));
    }

    @DisplayName("테이블 그룹 생성 실패 테스트 - 주문 테이블에 다른 테이블 그룹이 존재함")
    @Test
    void create_failure_alreadyExistTableGroup() {
        // given
        OrderTable 그룹에_속해있는_테이블1 = TableFixture.create(1L, 2L, 0, true);
        OrderTable 그룹에_속해있는_테이블2 = TableFixture.create(2L, 2L, 0, true);
        TableGroupRequest 요청_테이블_그룹 = TableGroupRequest.of(Arrays.asList(OrderTableIdRequest.of(그룹에_속해있는_테이블1.getId()), OrderTableIdRequest.of(그룹에_속해있는_테이블2.getId())));

        given(tableService.findAllByIdIn(anyList())).willReturn(Arrays.asList(그룹에_속해있는_테이블1, 그룹에_속해있는_테이블2));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(요청_테이블_그룹));
    }

    @DisplayName("테이블 그룹 생성 실패 테스트 - 주문 테이블이 비어있지 않음")
    @Test
    void create_failure_notEmpty() {
        // given
        TableGroupRequest 요청_테이블_그룹 = TableGroupRequest.of(Arrays.asList(OrderTableIdRequest.of(비어있지_않은_테이블1.getId()), OrderTableIdRequest.of(비어있지_않은_테이블2.getId())));

        given(tableService.findAllByIdIn(anyList())).willReturn(Arrays.asList(비어있지_않은_테이블1, 비어있지_않은_테이블2));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(요청_테이블_그룹));
    }

    @DisplayName("테이블 그룹 해제 성공 테스트")
    @Test
    void ungroup_success() {
        // given
        given(tableGroupDao.findById(any(Long.class))).willReturn(Optional.of(TableGroup.of(LocalDateTime.now())));
        given(tableService.findAllByTableGroup(any(TableGroup.class))).willReturn(Arrays.asList(그룹에_속해있는_테이블1, 그룹에_속해있는_테이블2));
        given(orderService.isCookingOrMealExists(any(OrderTables.class))).willReturn(false);

        // when
        tableGroupService.ungroup(그룹에_속해있는_테이블1.getTableGroupId());

        // then
        assertAll(
                () -> assertThat(그룹에_속해있는_테이블1.getTableGroupId()).isNull()
                , () -> assertThat(그룹에_속해있는_테이블2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("테이블 그룹 해제 실패 테스트 - 테이블 그룹에 속해있는 주문 테이블이 COOKING 또는 MEAL")
    @Test
    void ungroup_failure_orderStatus() {
        // given
        given(tableGroupDao.findById(any(Long.class))).willReturn(Optional.of(회사B_단체_테이블));
        given(tableService.findAllByTableGroup(any(TableGroup.class))).willReturn(Arrays.asList(그룹에_속해있는_테이블1, 그룹에_속해있는_테이블2));
        given(orderService.isCookingOrMealExists(any(OrderTables.class))).willReturn(true);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(회사A_단체_테이블.getId()));
    }
}
