package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.TableGenerator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.table.TableGenerator.주문_테이블_목록_생성;
import static kitchenpos.table.TableGenerator.테이블_그룹_생성_요청;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TableGroupServiceTest {

    @MockBean
    private TableService tableService;

    @MockBean
    private OrderService orderService;

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블 = TableGenerator.주문_테이블_생성(손님_수_생성(10));
    }

    @DisplayName("테이블 그룹 생성 시 2개 미만의 주문 테이블이 속해 있으면 예외가 발생해야 한다")
    @Test
    void createTableGroupByUnderTwoOrderTableTest() {
        // given
        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록_생성(Collections.singletonList(주문_테이블)));

        // then
        테이블_그룹_생성_실패됨(() -> tableGroupService.create(테이블_그룹_생성_요청(Collections.singletonList(0L))));

        // given
        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록_생성(Collections.emptyList()));

        // then
        테이블_그룹_생성_실패됨(() -> tableGroupService.create(테이블_그룹_생성_요청(Collections.emptyList())));
    }

    @DisplayName("저장되지 않은 주문 테이블을 포함한 테이블 그룹 생성 요청 시 예외가 발생해야 한다")
    @Test
    void createTableGroupByContainNotSavedOrderTableTest() {
        // given
        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블)));

        // then
        테이블_그룹_생성_실패됨(() -> tableGroupService.create(테이블_그룹_생성_요청(Arrays.asList(0L, 0L, 0L))));
    }

    @DisplayName("비어있지 않은 주문 테이블이 포함된 테이블 그룹 생성 요청 시 예외가 발생해야 한다")
    @Test
    void createTableGroupByContainNotEmptyOrderTableTest() {
        // given
        OrderTable 비어있지_않은_테이블 = TableGenerator.주문_테이블_생성(손님_수_생성(10));
        비어있지_않은_테이블.updateEmpty(false, OrderStatus.COMPLETION);
        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 비어있지_않은_테이블)));

        // then
        테이블_그룹_생성_실패됨(() -> tableGroupService.create(테이블_그룹_생성_요청(Arrays.asList(0L, 0L))));
    }

    @DisplayName("테이블 그룹 생성 시 이미 테이블 그룹에 포함된 주문 테이블이 포함되어 있으면 예외가 발생해야 한다")
    @Test
    void createTableGroupByContainAlreadyBelongTableGroupOrderTableTest() {
        // given
        OrderTable 테이블_그룹에_속해있는_주문_테이블 = TableGenerator.주문_테이블_생성(손님_수_생성(10));
        테이블_그룹에_속해있는_주문_테이블.joinGroup(new TableGroup(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 테이블_그룹에_속해있는_주문_테이블))));
        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 테이블_그룹에_속해있는_주문_테이블)));

        // then
        테이블_그룹_생성_실패됨(() -> tableGroupService.create(테이블_그룹_생성_요청(Arrays.asList(0L, 0L))));
    }

    @DisplayName("정상 조건으로 테이블 그룹 생성 시 정상 생성되어야 한다")
    @Test
    void createTableGroupTest() {
        // given
        TableGroupCreateRequest 테이블_그룹_생성_요청 = 테이블_그룹_생성_요청(Arrays.asList(0L, 0L));
        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블)));

        // when
        TableGroup 테이블_그룹 = tableGroupService.create(테이블_그룹_생성_요청);

        // then
        테이블_그룹_정상_성상됨(테이블_그룹, 주문_테이블, 2);
    }

    @DisplayName("테이블 그룹 해제시 테이블 그룹에 속해있는 주문 테이블 중 요리중 또는 식사중인 테이블이 포함되어 있으면 예외가 발생해야 한다")
    @Test
    void ungroupByIncludeCookingOrMealOrderStatusOrderTableTest() {
        // given
        TableGroupCreateRequest 테이블_그룹_생성_요청 = 테이블_그룹_생성_요청(Arrays.asList(0L, 0L));
        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블)));
        when(orderService.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);
        TableGroup 테이블_그룹 = tableGroupService.create(테이블_그룹_생성_요청);

        // then
        테이블_그룹_해제_실패됨(() -> tableGroupService.ungroup(테이블_그룹.getId()));
    }

//    @DisplayName("테이블 그룹 해제 시 정상 동작해야 한다")
//    @Test
//    void ungroupTest() {
//        // given
//        TableGroupCreateRequest 테이블_그룹_생성_요청 = 테이블_그룹_생성_요청(Arrays.asList(0L, 0L));
//        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블)));
//        when(orderService.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);
//        TableGroup 테이블_그룹 = tableGroupService.create(테이블_그룹_생성_요청);
//
//        // when
//        tableGroupService.ungroup(테이블_그룹.getId());
//
//        // then
//        테이블_그룹_해제_성공됨(테이블_그룹.getId());
//    }

    void 테이블_그룹_생성_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 테이블_그룹_정상_성상됨(TableGroup tableGroup, OrderTable orderTable, int expectedContainOrderTableCount) {
        assertThat(tableGroup.getId()).isNotNull();
        assertThat(tableGroup.getId()).isEqualTo(orderTable.getTableGroup().getId());
        assertThat(tableGroup.getOrderTables().size()).isEqualTo(expectedContainOrderTableCount);
        tableGroup.getOrderTables().forEach(table -> assertThat(table.isEmpty()).isFalse());
    }

    void 테이블_그룹_해제_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 테이블_그룹_해제_성공됨(Long id) {
//        TODO - failed to lazily initialize a collection of role 이슈 해결 할 수 있는 방법 문의하기
        assertThat(tableGroupService.getTableGroup(id).getOrderTables().size()).isEqualTo(0);
    }
}
