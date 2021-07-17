package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.TableService;
import kitchenpos.common.BaseControllerTest;
import kitchenpos.domain.OrderTable;

class TableRestControllerTest extends BaseControllerTest {

    @MockBean
    TableService mockTableService;

    OrderTable 테이블_1번, 테이블_2번;

    @BeforeEach
    void setUp() {
        테이블_1번 = new OrderTable();
        테이블_1번.setId(1L);
        테이블_1번.setEmpty(true);

        테이블_2번 = new OrderTable();
        테이블_2번.setId(2L);
        테이블_2번.setEmpty(false);
        테이블_2번.setNumberOfGuests(2);
    }

    @DisplayName("테이블 생성")
    @Test
    void create() throws Exception {
        String jsonString = objectMapper.writeValueAsString(테이블_1번);
        when(mockTableService.create(any())).thenReturn(테이블_1번);

        mockMvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonString)
        ).andDo(print())
        .andExpect(status().isCreated());
    }

    @DisplayName("전체 테이블 조회")
    @Test
    void list() throws Exception {
        when(mockTableService.list()).thenReturn(Arrays.asList(테이블_1번, 테이블_2번));

        mockMvc.perform(get("/api/products"))
            .andDo(print()) // 요청과 응답을 출력이 가능
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*]").isArray());
    }

    @DisplayName("빈테이블로 상태 변경")
    @Test
    void changeEmpty() throws Exception {
        when(mockTableService.changeEmpty(eq(테이블_1번.getId()), any())).thenReturn(테이블_1번);
        String jsonString = objectMapper.writeValueAsString(테이블_1번);

        mockMvc.perform(put("/api/tables/" + 테이블_1번.getId() + "/empty")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonString)
            )
            .andDo(print()) // 요청과 응답을 출력이 가능
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(테이블_1번.getId()))
            .andExpect(jsonPath("$.empty").value(true));
    }

    @DisplayName("테이블 인원 수 변경")
    @Test
    void changeNumberOfGuests() throws Exception {
        when(mockTableService.changeNumberOfGuests(eq(테이블_2번.getId()), any())).thenReturn(테이블_2번);
        String jsonString = objectMapper.writeValueAsString(테이블_2번);

        mockMvc.perform(put("/api/tables/" + 테이블_2번.getId() + "/number-of-guests")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonString)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(테이블_2번.getId()))
        .andExpect(jsonPath("$.numberOfGuests").value(2));
    }
}