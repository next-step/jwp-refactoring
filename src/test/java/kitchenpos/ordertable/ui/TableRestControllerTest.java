package kitchenpos.ordertable.ui;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.CommonTestFixtures;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    private static final String BASE_PATH = "/api/tables";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @DisplayName("주문 테이블 등록")
    @Test
    void create() throws Exception {
        //given
        int numOfGuests = 6;
        boolean empty = false;
        OrderTable requestOrderTable = new OrderTable(numOfGuests, empty);
        OrderTableResponse expectedOrderTable = OrderTableResponse.from(
            new OrderTable(1L, numOfGuests, empty));
        given(tableService.create(any())).willReturn(expectedOrderTable);

        //when, then
        mockMvc.perform(post(BASE_PATH)
                .content(CommonTestFixtures.asJsonString(requestOrderTable))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(expectedOrderTable.getId()));
    }

    @DisplayName("주문 테이블 조회")
    @Test
    void list() throws Exception {
        //given
        List<OrderTableResponse> expectedOrderTables = Arrays.asList(
            OrderTableResponse.from(new OrderTable(1L, 6, false)),
            OrderTableResponse.from(new OrderTable(2L, 3, false)));

        given(tableService.list()).willReturn(expectedOrderTables);

        //when, then
        mockMvc.perform(get(BASE_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*]['id']",
                containsInAnyOrder(
                    expectedOrderTables.stream()
                        .mapToInt(t -> t.getId().intValue())
                        .boxed()
                        .toArray(Integer[]::new))));
    }

    @DisplayName("테이블 상태 변경")
    @Test
    void changeEmpty() throws Exception {
        //given
        boolean changeEmpty = false;
        OrderTable requestOrderTable = new OrderTable(changeEmpty);
        OrderTableResponse expectedOrderTable = OrderTableResponse.from(
            new OrderTable(1L, 1L, 6, changeEmpty));
        given(tableService.changeEmpty(any(), any())).willReturn(expectedOrderTable);

        //when,then
        mockMvc.perform(put(BASE_PATH + "/{orderTableId}/empty",
                expectedOrderTable.getTableGroupId()).content(
                    CommonTestFixtures.asJsonString(requestOrderTable))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(expectedOrderTable.getId()))
            .andExpect(jsonPath("$.empty").value(expectedOrderTable.isEmpty()));
    }

    @DisplayName("테이블 방문자 수 변경")
    @Test
    void changeNumberOfGuests() throws Exception {
        //given
        int numberOfGuests = 5;
        OrderTable requestOrderTable = new OrderTable(numberOfGuests);
        OrderTableResponse expectedOrderTable = OrderTableResponse.from(
            new OrderTable(1L, 1L, numberOfGuests, false));
        given(tableService.changeNumberOfGuests(any(), any())).willReturn(expectedOrderTable);

        //when,then
        mockMvc.perform(put(BASE_PATH + "/{numberOfGuests}/number-of-guests",
                expectedOrderTable.getTableGroupId()).content(
                    CommonTestFixtures.asJsonString(requestOrderTable))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(expectedOrderTable.getId()))
            .andExpect(jsonPath("$.numberOfGuests").value(expectedOrderTable.getNumberOfGuests()));
    }
}
