package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;

    OrderTable createdOrderTable;
    OrderTable createdOrderTable2;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        createdOrderTable = tableService.create(orderTable);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        createdOrderTable2 = tableService.create(orderTable2);
    }

    @Test
    @DisplayName("table group 생성")
    void table_create_test() {
        //given
        TableGroup tableGroupRequest = TABLE_GROUP_REQUEST_생성();
        tableGroupRequest.setOrderTables(Arrays.asList(createdOrderTable, createdOrderTable2));
        //when
        TableGroup createdTableGroup = TABLE_GROUP_생성_테스트(tableGroupRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdTableGroup.getId()).isNotNull();
        });
    }


    private TableGroup TABLE_GROUP_생성_테스트(TableGroup tableGroupRequest) {
        return tableGroupService.create(tableGroupRequest);
    }

    private TableGroup TABLE_GROUP_REQUEST_생성() {
        TableGroup tableGroup = new TableGroup();
        return tableGroup;
    }
}
