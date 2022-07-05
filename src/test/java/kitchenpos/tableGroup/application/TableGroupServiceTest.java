package kitchenpos.tableGroup.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.TableGenerator;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tableGroup.application.TableGroupService;
import kitchenpos.tableGroup.dao.TableGroupRepository;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupCreateRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import kitchenpos.tableGroup.mapper.TableGroupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.table.TableGenerator.*;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static kitchenpos.tableGroup.TableGroupGenerator.테이블_그룹_생성_요청;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    private OrderTable 주문_테이블1, 주문_테이블2;
    private List<Long> 주문_테이블_아이디들 = new ArrayList<>();

    @BeforeEach
    void setUp() {
        주문_테이블_아이디들.clear();

        주문_테이블_아이디들.add(tableService.create(주문_테이블_생성_요청(10)).getId());
        주문_테이블_아이디들.add(tableService.create(주문_테이블_생성_요청(10)).getId());
        주문_테이블_아이디들.add(tableService.create(주문_테이블_생성_요청(10)).getId());
    }

    @DisplayName("단체 지정 생성 시 2개 미만의 주문 테이블이 속해 있으면 예외가 발생해야 한다")
    @Test
    void createTableGroupByUnderTwoOrderTableTest() {
        테이블_그룹_생성_실패됨(() -> tableGroupService.create(테이블_그룹_생성_요청(Collections.singletonList(주문_테이블_아이디들.get(0)))));
        테이블_그룹_생성_실패됨(() -> tableGroupService.create(테이블_그룹_생성_요청(Collections.emptyList())));
    }

    @DisplayName("저장되지 않은 주문 테이블을 포함한 단체 지정 생성 요청 시 예외가 발생해야 한다")
    @Test
    void createTableGroupByContainNotSavedOrderTableTest() {
        // given
        Long 저장되지_않은_테이블_아이디 = 0L;

        // then
        테이블_그룹_생성_실패됨(() -> tableGroupService.create(테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 저장되지_않은_테이블_아이디))));
    }

//    @DisplayName("비어있지 않은 주문 테이블이 포함된 단체 지정 생성 요청 시 예외가 발생해야 한다")
//    @Test
//    void createTableGroupByContainNotEmptyOrderTableTest() {
//        // given
//        빈_테이블_변경_API_호출(주문_테이블_아이디들.get(0), false);
//
//        // then
//        테이블_그룹_생성_실패됨(() -> tableGroupService.create(테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 주문_테이블_아이디들.get(1)))));
//    }

    @DisplayName("단체 지정 생성 시 이미 단체 지정에 포함된 주문 테이블이 포함되어 있으면 예외가 발생해야 한다")
    @Test
    void createTableGroupByContainAlreadyBelongTableGroupOrderTableTest() {
        // given
        tableGroupService.create(테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 주문_테이블_아이디들.get(1))));

        // then
        테이블_그룹_생성_실패됨(() -> tableGroupService.create(테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 주문_테이블_아이디들.get(2)))));
    }

    @DisplayName("정상 조건으로 단체 지정 생성 시 정상 생성되어야 한다")
    @Test
    void createTableGroupTest() {
        // given
        TableGroupCreateRequest 테이블_그룹_생성_요청 = 테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 주문_테이블_아이디들.get(1)));

        // when
        TableGroupResponse 테이블_그룹 = tableGroupService.create(테이블_그룹_생성_요청);

        // then
        테이블_그룹_정상_성상됨(테이블_그룹, 2);
    }

//    @DisplayName("단체 지정 해제시 단체 지정에 속해있는 주문 테이블 중 요리중 또는 식사중인 테이블이 포함되어 있으면 예외가 발생해야 한다")
//    @Test
//    void ungroupByIncludeCookingOrMealOrderStatusOrderTableTest() {
//        // given
//        Long 테이블_그룹_아이디 = tableGroupService.create(테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 주문_테이블_아이디들.get(1)))).getId();
//
//
//
//        TableGroupCreateRequest 테이블_그룹_생성_요청 = 테이블_그룹_생성_요청(Arrays.asList(0L, 0L));
//        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블)));
//        when(orderService.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);
//        TableGroupResponse 테이블_그룹 = tableGroupService.create(테이블_그룹_생성_요청);
//
//        // then
//        테이블_그룹_해제_실패됨(() -> tableGroupService.ungroup(테이블_그룹.getId()));
//    }

    @DisplayName("단체 지정 해제 시 정상 동작해야 한다")
    @Test
    void ungroupTest() {
        // given
        Long 테이블_그룹_아이디 = tableGroupService.create(테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 주문_테이블_아이디들.get(1)))).getId();

        // then
        테이블_그룹_해제_성공됨(() -> tableGroupService.ungroup(테이블_그룹_아이디));
    }

    void 테이블_그룹_생성_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 테이블_그룹_정상_성상됨(TableGroupResponse tableGroup, int expectedContainOrderTableCount) {
        assertThat(tableGroup.getId()).isNotNull();
        assertThat(tableGroup.getOrderTableResponses().size()).isEqualTo(expectedContainOrderTableCount);
        tableGroup.getOrderTableResponses().forEach(table -> assertThat(table.isEmpty()).isFalse());
    }

//    void 테이블_그룹_해제_실패됨(Runnable runnable) {
//        assertThatIllegalArgumentException().isThrownBy(runnable::run);
//    }

    void 테이블_그룹_해제_성공됨(Runnable runnable) {
        assertThatNoException().isThrownBy(runnable::run);
    }
}
