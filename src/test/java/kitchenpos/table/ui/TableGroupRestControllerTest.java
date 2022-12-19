package kitchenpos.table.ui;

import kitchenpos.ControllerTest;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.OrderTablesException;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TableGroupRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class TableGroupRestControllerTest extends ControllerTest {
    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블그룹 생성을 요청하면 생성된 테이블그룹응답")
    @Test
    public void returnTableGroup() throws Exception {
        TableGroupResponse tableGroup = getTableGroup();
        doReturn(tableGroup).when(tableGroupService).create(any(TableGroupRequest.class));

        webMvc.perform(post("/api/table-groups")
                        .content(mapper.writeValueAsString(TableGroup.builder().build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(tableGroup.getId().intValue())))
                .andExpect(status().isCreated());
    }

    @DisplayName("테이블그룹 생성을 요청하면 실패응답")
    @Test
    public void throwsExceptionWhenTableGroupCreate() throws Exception {
        doThrow(new IllegalArgumentException()).when(tableGroupService).create(any(TableGroupRequest.class));

        webMvc.perform(post("/api/table-groups")
                        .content(mapper.writeValueAsString(TableGroup.builder().build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("테이블그룹 생성중 테이블개수가 2개 미만이면 실패응답")
    @Test
    public void throwsExceptionWhenTableGroupCreateWithLessThen2Table() throws Exception {
        doThrow(new OrderTablesException("테이블 사이즈는 2개 이상만 허용합니다")).when(tableGroupService).create(any(TableGroupRequest.class));

        webMvc.perform(post("/api/table-groups")
                        .content(mapper.writeValueAsString(TableGroup.builder().build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is("테이블 사이즈는 2개 이상만 허용합니다")))
                .andExpect(status().isBadRequest());
    }


    @DisplayName("테이블그룹 해재를 요청하면 성공응답")
    @Test
    public void unTableGroup() throws Exception {
        TableGroup tableGroup = TableGroup.builder().id(13l)
                .build();
        webMvc.perform(delete("/api/table-groups/" + tableGroup.getId()))
                .andExpect(status().isNoContent());
    }

    @DisplayName("테이블그룹 해재를 요청하면 실패응답")
    @Test
    public void throwsExceptionWhenUnTableGroup() throws Exception {
        TableGroup tableGroup = TableGroup.builder().id(13l)
                .build();
        doThrow(new IllegalArgumentException()).when(tableGroupService).ungroup(anyLong());
        webMvc.perform(delete("/api/table-groups/" + tableGroup.getId()))
                .andExpect(status().isBadRequest());
    }

    private TableGroupResponse getTableGroup() {
        return TableGroupResponse.of(TableGroup.builder()
                .id(Arbitraries.longs().between(1, 100).sample())
                .build());
    }

}

