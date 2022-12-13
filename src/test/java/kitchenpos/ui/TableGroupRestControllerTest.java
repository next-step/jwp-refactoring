package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TableGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("단체지정을 등록한다")
    @Test
    Long 단체_지정() throws Exception {
        List<Long> tableIds = Arrays.asList(1L, 2L);
        TableGroup group = new TableGroup(tableIds.stream().map(OrderTable::new).collect(Collectors.toList()));

        MvcResult result = mockMvc.perform(post("/api/table-groups")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(group)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.createdDate").isNotEmpty())
            .andExpect(jsonPath("$.orderTables.length()").value(tableIds.size()))
            .andReturn();

        Long id = getId(result);
        assertThat(tableGroupRepository.findById(id)).isNotEmpty();
        return id;
    }

    @DisplayName("단체지정을 해제한다")
    @Test
    void group2() throws Exception {
        Long tableGroupId = 단체_지정();

        mockMvc.perform(delete("/api/table-groups/" + tableGroupId))
            .andDo(print())
            .andExpect(status().isNoContent());

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            assertThat(orderTable.getTableGroupId()).isNull();
        }
    }

    private Long getId(MvcResult result) throws
        com.fasterxml.jackson.core.JsonProcessingException,
        UnsupportedEncodingException {
        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, TableGroup.class).getId();
    }

}
