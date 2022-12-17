package kitchenpos.tablegroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableService;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.CreateTableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.table.domain.OrderTableTest.두_명의_방문객이_존재하는_테이블;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("단체 지정 ui 테스트")
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() throws Exception {
        //given:
        final List<Long> 주문_테이블_id_목록 = Arrays.asList(
                tableService.create(두_명의_방문객이_존재하는_테이블()).getId(),
                tableService.create(두_명의_방문객이_존재하는_테이블()).getId());

        //when:
        assertThatNoException().isThrownBy(() -> mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(new CreateTableGroupRequest(주문_테이블_id_목록))))
                .andDo(print())
                .andExpect(status().isCreated()));
        //then:
    }

    @Transactional
    @DisplayName("단체 지정 해제 성공")
    @Test
    void 단체_지정_해제_성공() {
        //given:
        final TableGroup 저장된_단체_지정_테이블 = tableGroupService.create(
                new CreateTableGroupRequest(Arrays.asList(
                        tableService.create(두_명의_방문객이_존재하는_테이블()).getId(),
                        tableService.create(두_명의_방문객이_존재하는_테이블()).getId())));
        //when,then:
        assertThatNoException().isThrownBy(() ->
                mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 저장된_단체_지정_테이블.getId()))
                        .andDo(print())
                        .andExpect(status().isNoContent()));
    }
}
