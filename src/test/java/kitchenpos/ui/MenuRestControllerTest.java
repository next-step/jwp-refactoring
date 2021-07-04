package kitchenpos.ui;

import static kitchenpos.utils.UnitTestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("메뉴 통합 테스트")
class MenuRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("메뉴를 생성한다")
    void create() throws Exception {
        MenuProductRequest menuProduct = new MenuProductRequest(1L, 2);
        MenuRequest request = new MenuRequest("후라이드+후라이드",
            BigInteger.valueOf(19000), 1L, Arrays.asList(menuProduct));
        String payload = objectMapper.writeValueAsString(행복세트);
        mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("메뉴를 목록을 가져온다")
    void list() throws Exception {
        mockMvc.perform(get("/api/menus"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$[2].id").value(3))
            .andExpect(jsonPath("$[2].name").value("반반치킨"))
            .andExpect(jsonPath("$[2].price").value(16000))
            .andExpect(jsonPath("$[2].menuGroupId").value(2));
    }
}
