package kitchenpos.table.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.IntegrationTestHelper;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.ui.TableRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends IntegrationTestHelper {

    @MockBean
    private TableService tableService;

    @Autowired
    private TableRestController tableRestController;

    @Override
    protected Object controller() {
        return tableRestController;
    }

    @DisplayName("테이블 생성 요청")
    @Test
    void createTest() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(1l, 3);
        Mockito.when(tableService.create(any())).thenReturn(orderTable);

        // when
        ResultActions resultActions = 테이블_생성_요청(orderTable);

        // then
        테이블_생성_성공(resultActions);
    }

    @DisplayName("테이블 조회 요청")
    @Test
    void listTest() throws Exception {
        // given
        OrderTable orderTable1 = new OrderTable(1l, 3);
        OrderTable orderTable2 = new OrderTable(2l, 5);
        Mockito.when(tableService.list()).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        ResultActions resultActions = 테이블_조회_요청();

        // then
        MvcResult mvcResult = 테이블_조회_성공(resultActions);
        List<OrderTable> orderTables = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<OrderTable>>(){});
        assertThat(orderTables).isNotEmpty().hasSize(2);
    }

    @DisplayName("테이블을 빈 상태로 변경 요청")
    @Test
    void changeEmptyTest() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(1l, 3);
        orderTable.setEmpty(true);
        Mockito.when(tableService.changeEmpty(1l, orderTable)).thenReturn(orderTable);

        // when
        ResultActions resultActions = 테이블_빈상태_변경_요청(1l, orderTable);

        // then
        테이블_변경_성공(resultActions);
    }

    @DisplayName("테이블 손님수 변경 요청")
    @Test
    void changeNumberOfGuestsTest() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(1l, 5);
        orderTable.setEmpty(true);
        Mockito.when(tableService.changeNumberOfGuests(1l, orderTable)).thenReturn(orderTable);

        // when
        ResultActions resultActions = 테이블_손님수_변경_요청(1l, orderTable);

        // then
        테이블_변경_성공(resultActions);
    }



    private ResultActions 테이블_생성_요청(OrderTable orderTable) throws Exception {
        return postRequest("/api/tables", orderTable);
    }

    private MvcResult 테이블_생성_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isCreated()).andReturn();
    }

    private ResultActions 테이블_조회_요청() throws Exception {
        return getRequest("/api/tables");
    }

    private MvcResult 테이블_조회_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk()).andReturn();
    }

    private ResultActions 테이블_빈상태_변경_요청(final long orderTableId, final OrderTable orderTable) throws Exception {
        String uri = String.format("/api/tables/%s/empty", orderTableId);
        return putRequest(uri, orderTable);
    }

    private MvcResult 테이블_변경_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk()).andReturn();
    }

    private ResultActions 테이블_손님수_변경_요청(final long orderTableId, final OrderTable orderTable) throws Exception {
        String uri = String.format("/api/tables/%s/number-of-guests", orderTableId);
        return putRequest(uri, orderTable);
    }
}
