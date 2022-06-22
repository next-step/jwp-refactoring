package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 등록 한다.")
    @Test
    void create() {
        //given
        OrderTable emptyTable1 = new OrderTable(1L, null, 0, true);
        OrderTable emptyTable2 = new OrderTable(2L, null, 0, true);
        TableGroup request = new TableGroup(null, null, Arrays.asList(emptyTable1, emptyTable2));

        //when
        TableGroup result = tableGroupService.create(request);

        //then
        List<OrderTable> orderTables = result.getOrderTables();
        assertThat(orderTables.get(0).isEmpty()).isFalse();
        assertThat(orderTables.get(0).getTableGroupId()).isNotNull();
        assertThat(orderTables.get(1).isEmpty()).isFalse();
        assertThat(orderTables.get(1).getTableGroupId()).isNotNull();
    }

    @DisplayName("테이블이 2개 미만이거나 비어있으면 단체 지정을 등록 할 수 없다.")
    @Test
    void create_empty_or_less_then_two() {
        //given
        OrderTable emptyTable3 = new OrderTable(3L, null, 0, true);
        TableGroup request_single = new TableGroup(null, null, Collections.singletonList(emptyTable3));
        TableGroup request_empty = new TableGroup(null, null, Collections.emptyList());

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
        OrderTable emptyTable1 = new OrderTable(1L, null, 0, true);
        OrderTable not_registered_table = new OrderTable(9999999L, null, 0, true);
        TableGroup request = new TableGroup(null, null, Arrays.asList(emptyTable1, not_registered_table));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }


    @DisplayName("주문 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_in_order_table() {
        //given
        OrderTable orderTable1 = orderTableDao.save( new OrderTable(10L, null, 3, false));
        OrderTable emptyTable1 = new OrderTable(2L, null, 0, true);
        TableGroup request = new TableGroup(null, null, Arrays.asList(orderTable1, emptyTable1));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }


    @DisplayName("이미 단체 지정된 테이블이 있는 경우 단체 지정 등록 할 수 없다.")
    @Test
    void create_already() {
        //given
        OrderTable emptyTable3 = new OrderTable(3L, null, 0, true);
        OrderTable emptyTable4 = new OrderTable(4L, null, 0, true);
        OrderTable emptyTable5 = new OrderTable(5L, null, 0, true);
        tableGroupService.create(new TableGroup(null, null, Arrays.asList(emptyTable3, emptyTable4)));

        TableGroup request = new TableGroup(null, null, Arrays.asList(emptyTable3, emptyTable5));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("단체 지정을 해제 한다.")
    @Test
    void ungroup() {
        //given
        OrderTable emptyTable4 = new OrderTable(5L, null, 0, true);
        OrderTable emptyTable5 = new OrderTable(6L, null, 0, true);
        TableGroup tableGroup = tableGroupService
                .create(new TableGroup(null, null, Arrays.asList(emptyTable4, emptyTable5)));

        //when
        tableGroupService.ungroup(tableGroup.getId());

        //then
        List<OrderTable> results = orderTableDao.findAllByTableGroupId(tableGroup.getId());
        assertThat(results).isEmpty();
    }

}
