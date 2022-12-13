package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.TableServiceTest.두_명의_방문객;
import static kitchenpos.application.TableServiceTest.빈_상태;
import static kitchenpos.application.TableServiceTest.주문_테이블;
import static kitchenpos.application.TableGroupServiceTest.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
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
        final List<OrderTable> 주문_테이블_목록 = Arrays.asList(
                tableService.create(주문_테이블(두_명의_방문객, 빈_상태)),
                tableService.create(주문_테이블(두_명의_방문객, 빈_상태)));

        final TableGroup 단체_지정_테이블 = 단체_지정(LocalDateTime.now(), 주문_테이블_목록);
        //when:
        final TableGroup 저장된_단체_지정_테이블 = mapper.readValue(
                mockMvc.perform(post("/api/table-groups")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(단체_지정_테이블)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(), TableGroup.class);
        //then:
        assertThat(저장된_단체_지정_테이블.getOrderTables()).hasSize(주문_테이블_목록.size());
    }

    @DisplayName("단체 지정 해제 성공")
    @Test
    void 단체_지정_해제_성공() {
        //given:
        final TableGroup 저장된_단체_지정_테이블 = tableGroupService.create(단체_지정(LocalDateTime.now(), Arrays.asList(
                tableService.create(주문_테이블(두_명의_방문객, 빈_상태)),
                tableService.create(주문_테이블(두_명의_방문객, 빈_상태)))));
        //when,then:
        assertThatNoException().isThrownBy(() ->
                mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 저장된_단체_지정_테이블.getId()))
                        .andDo(print())
                        .andExpect(status().isNoContent()));
    }
}
