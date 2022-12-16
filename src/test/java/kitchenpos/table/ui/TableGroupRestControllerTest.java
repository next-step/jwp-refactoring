package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableBag;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.table.application.TableServiceTest.주문_테이블;
import static kitchenpos.order.domain.OrderTableTest.두_명의_방문객;
import static kitchenpos.order.domain.OrderTableTest.빈_상태;
import static kitchenpos.table.domain.TableGroupTest.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
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
        final OrderTableBag 주문_테이블_목록 = OrderTableBag.from(Arrays.asList(
                tableService.create(주문_테이블(두_명의_방문객, 빈_상태)),
                tableService.create(주문_테이블(두_명의_방문객, 빈_상태))));

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
        assertThat(저장된_단체_지정_테이블.orderTables().stream().noneMatch(OrderTable::isEmpty)).isTrue();
    }

    @DisplayName("단체 지정 해제 성공")
    @Test
    void 단체_지정_해제_성공() {
        //given:
        final TableGroup 저장된_단체_지정_테이블 = tableGroupService.create(
                단체_지정(LocalDateTime.now(), OrderTableBag.from(Arrays.asList(
                        tableService.create(주문_테이블(두_명의_방문객, 빈_상태)),
                        tableService.create(주문_테이블(두_명의_방문객, 빈_상태))))));
        //when,then:
        assertThatNoException().isThrownBy(() ->
                mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 저장된_단체_지정_테이블.getId()))
                        .andDo(print())
                        .andExpect(status().isNoContent()));
    }
}
