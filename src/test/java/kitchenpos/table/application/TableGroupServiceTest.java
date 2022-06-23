package kitchenpos.table.application;

import static kitchenpos.helper.TableFixtures.테이블_요청_만들기;
import static kitchenpos.helper.TableGroupFixtures.테이블_그룹_요청_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.ir.annotations.Ignore;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DisplayName("단체 지정 관련 Service 기능 테스트")
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TableGroupServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 등록 한다.")
    @Test
    void create() {
        //given
        OrderTableRequest emptyTable1 = 테이블_요청_만들기(1L);
        OrderTableRequest emptyTable2 = 테이블_요청_만들기(2L);
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(emptyTable1, emptyTable2));

        //when
        TableGroupResponse result = tableGroupService.create(request);

        //then
        List<OrderTableResponse> orderTables = result.getOrderTables();
        assertThat(orderTables.get(0).getEmpty()).isFalse();
        assertThat(orderTables.get(0).getTableGroupId()).isNotNull();
        assertThat(orderTables.get(1).getEmpty()).isFalse();
        assertThat(orderTables.get(1).getTableGroupId()).isNotNull();
    }

    @DisplayName("테이블이 2개 미만이면 단체 지정을 등록 할 수 없다.")
    @Test
    void create_less_then_two() {
        //given
        OrderTableRequest emptyTable1 = 테이블_요청_만들기(3L);
        TableGroupRequest request_single = 테이블_그룹_요청_만들기(Arrays.asList(emptyTable1));
        TableGroupRequest request_empty = 테이블_그룹_요청_만들기(Collections.emptyList());

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request_single));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request_empty));
    }


    @DisplayName("등록 되어 있지 않은 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_not_registered_table() {
        //given
        OrderTableRequest not_registered_table = 테이블_요청_만들기(99999L);
        OrderTableRequest emptyTable = 테이블_요청_만들기(3L);
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(not_registered_table, emptyTable));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }


    @DisplayName("주문 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_in_order_table() {
        //given
        kitchenpos.table.domain.OrderTable orderTable = orderTableRepository.save(new kitchenpos.table.domain.OrderTable(null, 3, false));
        OrderTableRequest orderTable1 = 테이블_요청_만들기(orderTable.getId());
        OrderTableRequest emptyTable1 = 테이블_요청_만들기(3L);
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(orderTable1, emptyTable1));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }


    @DisplayName("이미 단체 지정된 테이블이 있는 경우 단체 지정 등록 할 수 없다.")
    @Test
    void create_already() {
        //given
        OrderTableRequest emptyTable1 = 테이블_요청_만들기(3L);
        OrderTableRequest emptyTable2 = 테이블_요청_만들기(4L);
        OrderTableRequest emptyTable3 = 테이블_요청_만들기(5L);
        TableGroupRequest request1 = 테이블_그룹_요청_만들기(Arrays.asList(emptyTable1, emptyTable2));
        tableGroupService.create(request1);
        TableGroupRequest request2 = 테이블_그룹_요청_만들기(Arrays.asList(emptyTable1, emptyTable3));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request2));
    }

    @Ignore
    @DisplayName("단체 지정을 해제 한다.")
    @Test
    void ungroup() {
        //given
//        OrderTable emptyTable4 = new OrderTable(5L, null, 0, true);
//        OrderTable emptyTable5 = new OrderTable(6L, null, 0, true);
//        TableGroup tableGroup = tableGroupService
//                .create(new TableGroup(null, null, Arrays.asList(emptyTable4, emptyTable5)));
//
//        //when
//        tableGroupService.ungroup(tableGroup.getId());
//
//        //then
//        List<OrderTable> results = orderTableDao.findAllByTableGroupId(tableGroup.getId());
//        assertThat(results).isEmpty();
    }

}
