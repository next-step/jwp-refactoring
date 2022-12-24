package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        OrderTable orderTable1 = 주문_테이블_생성(null, 3, true);
        OrderTable orderTable2 = 주문_테이블_생성(null, 3, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        TableGroup response = tableGroupService.create(tableGroup);

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("주문 테이블의 수가 0일 수 없고 크기가 2보다 작을 수 없다.")
    @Test
    void createTableGroupOrderTableSizeZeroOrUnderTwoException() {
        OrderTable orderTable = 주문_테이블_생성(null, 3, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장되어 있는 주문 테이블의 수가 요청에서의 주문 테이블의 수가 다를 수 없다.")
    @Test
    void createTableGroupSavedOrderTableSizeRequestNotMatchException() {
        OrderTable orderTable = 주문_테이블_생성(null, 3, true);
        OrderTable requestOrderTable = new OrderTable(null, 3, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable, requestOrderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장되어 있는 주문 테이블이 비어있지 않거나 단체 지정 아이디가 값이 있을 수 없다.")
    @Test
    void createTableGroupSavedOrderTableNotEmptyOrHasTableGroupIdException() {
        OrderTable orderTable1 = 주문_테이블_생성(null, 3, true);
        OrderTable orderTable2 = 주문_테이블_생성(null, 3, false);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        OrderTable orderTable1 = 주문_테이블_생성(null, 3, true);
        OrderTable orderTable2 = 주문_테이블_생성(null, 3, true);
        TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now()
                , Arrays.asList(orderTable1, orderTable2)));

        tableGroupService.ungroup(tableGroup.getId());

        assertThatNoException();
    }

    private OrderTable 주문_테이블_생성(Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(tableGroupId, numberOfGuests, empty);
        return orderTableDao.save(orderTable);
    }
}
