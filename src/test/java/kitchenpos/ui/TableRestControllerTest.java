package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.NumberOfGeusts;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TableRestControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TableService tableService;

    @Autowired
    OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("테이블 생성 Api 테스트")
    @Test
    void create() throws Exception {
        OrderTable orderTable = new OrderTable(4, false);

        String requestBody = objectMapper.writeValueAsString(orderTable);

        mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    @DisplayName("테이블 목록 Api 테스트")
    @Test
    void list() throws Exception {
        long countOfOrderTable = orderTableRepository.count();

        mockMvc.perform(get("/api/tables")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize((int)countOfOrderTable)))
        ;
    }

    @DisplayName("테이블 정리 Api 테스트")
    @Test
    void changeEmpty() throws Exception {
        OrderTable orderTable = new OrderTable(4, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);
        savedOrderTable.changeEmpty(orderTableRequest);

        String requestBody = objectMapper.writeValueAsString(orderTableRequest);

        OrderTableResponse responseOrderTable = OrderTableResponse.of(savedOrderTable);
        String responseBody = objectMapper.writeValueAsString(responseOrderTable);

        mockMvc.perform(put("/api/tables/" + savedOrderTable.getId() + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody))
        ;
    }

    @DisplayName("테이블 인원수 변경 Api 테스트")
    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTable orderTable = new OrderTable(4, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        savedOrderTable.changeNumberOfGuests(NumberOfGeusts.of(2));

        OrderTable changeGuestCountOrderTable = new OrderTable(2, false);
        String requestBody = objectMapper.writeValueAsString(changeGuestCountOrderTable);

        OrderTableResponse responseOrderTable = OrderTableResponse.of(savedOrderTable);
        String responseBody = objectMapper.writeValueAsString(responseOrderTable);

        mockMvc.perform(put("/api/tables/" + savedOrderTable.getId() + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody))
        ;
    }

}
