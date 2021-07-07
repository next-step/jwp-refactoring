package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.dto.OrderTableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TableGroupRestControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @MockBean
    TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("테이블 그룹 생성 Api 테스트")
    @Test
    void create() throws Exception {
        OrderTable initOrderTable = new OrderTable(3, false);
        OrderTable savedOrderTable1 = orderTableRepository.save(initOrderTable);
        OrderTable savedOrderTable2 = orderTableRepository.save(initOrderTable);
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(savedOrderTable1.getId(), null, savedOrderTable1.getNumberOfGuests(), savedOrderTable1.isEmpty());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(savedOrderTable2.getId(), null, savedOrderTable2.getNumberOfGuests(), savedOrderTable2.isEmpty());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(LocalDateTime.now(), Arrays.asList(orderTableRequest1, orderTableRequest2));

        String requestBody = objectMapper.writeValueAsString(tableGroupRequest);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2));
        TableGroup saveTableGroup = tableGroupRepository.save(tableGroup);

        TableGroupResponse responseTableGroup = TableGroupResponse.of(saveTableGroup);
        String responseBody = objectMapper.writeValueAsString(responseTableGroup);

        when(tableGroupService.create(any())).thenReturn(responseTableGroup);
        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(responseBody))
        ;
    }

    @DisplayName("테이블 그룹 해제 Api 테스트")
    @Test
    void ungroup() throws Exception {
        Long tableGroupId = 1L;
        mockMvc.perform(delete("/api/table-groups/" + tableGroupId)
        )
                .andDo(print())
                .andExpect(status().isNoContent())
        ;
    }
}
