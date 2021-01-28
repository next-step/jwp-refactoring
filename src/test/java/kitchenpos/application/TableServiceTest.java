package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("table 생성")
    void table_create_test() {
        //given
        OrderTable tableRequest = TABLE_REQUEST_생성();
        //when
        OrderTable createdTable = TABLE_생성_테스트(tableRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdTable.getId()).isNotNull();
        });
    }

    @Test
    @DisplayName("table 리스트 조회")
    void table_show_test() {
        //given
        OrderTable createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성());
        OrderTable createdTable2 = TABLE_생성_테스트(TABLE_REQUEST_생성());
        //when
        List<OrderTable> list = TABLE_조회_테스트();
        //then
        Assertions.assertAll(() -> {
            List<Long> createdIds = list.stream().map(OrderTable::getId).collect(Collectors.toList());
            List<Long> requestIds = Arrays.asList(createdTable.getId(), createdTable2.getId());
            assertThat(createdIds).containsAll(requestIds);
        });
    }

    private List<OrderTable> TABLE_조회_테스트() {
        return tableService.list();
    }


    private OrderTable TABLE_생성_테스트(OrderTable tableRequest) {
        return tableService.create(tableRequest);
    }

    private OrderTable TABLE_REQUEST_생성() {
        OrderTable table = new OrderTable();
        return table;
    }
}
