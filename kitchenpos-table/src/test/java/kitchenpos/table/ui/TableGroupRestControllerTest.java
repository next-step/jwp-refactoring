package kitchenpos.table.ui;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.IntegrationTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

class TableGroupRestControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    private List<OrderTable> tables = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.tables.add(orderTableRepository.save(new OrderTable(0, true)));
        this.tables.add(orderTableRepository.save(new OrderTable(0, true)));
    }

    @DisplayName("단체지정을 등록한다")
    @Test
    void 단체_지정() throws Exception {
        Long id = 단체지정_등록();
        assertThat(tableGroupRepository.findById(id)).isNotEmpty();
    }

    @DisplayName("단체지정을 해제한다")
    @Test
    void group2() throws Exception {
        Long id = 단체지정_등록();

        mockMvc.perform(delete("/api/table-groups/" + id))
            .andDo(print())
            .andExpect(status().isNoContent());

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(id);
        for (OrderTable orderTable : orderTables) {
            assertThat(orderTable.getTableGroup()).isNull();
        }
    }

    private Long 단체지정_등록() throws Exception {
        TableGroupRequest request = new TableGroupRequest(
            tables.stream().map(OrderTable::getId)
                .collect(toList()));

        MvcResult result = mockMvc.perform(post("/api/table-groups")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.createdDate").isNotEmpty())
            .andExpect(jsonPath("$.orderTables.length()").value(tables.size()))
            .andReturn();

        return getId(result);
    }

    private Long getId(MvcResult result) throws
        com.fasterxml.jackson.core.JsonProcessingException,
        UnsupportedEncodingException {
        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, TableGroupResponse.class).getId();
    }

}
