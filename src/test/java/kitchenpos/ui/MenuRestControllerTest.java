package kitchenpos.ui;

import static kitchenpos.domain.MenuGroupTest.*;
import static kitchenpos.domain.MenuTest.*;
import static kitchenpos.domain.ProductTest.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = BEFORE_CLASS)
@DisplayName("메뉴 통합 테스트")
class MenuRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("메뉴를 생성한다")
    void create() throws Exception {
        MenuProductRequest 양념 = new MenuProductRequest(양념치킨.getId(), 1);
        MenuProductRequest 간장 = new MenuProductRequest(간장치킨.getId(), 1);
        MenuRequest request = new MenuRequest(
            "양념+간장", BigDecimal.valueOf(33000), 두마리메뉴.getId(), Arrays.asList(양념, 간장));
        mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(request.getName()))
            .andExpect(jsonPath("$.price").value(request.getPrice().longValue()))
            .andExpect(jsonPath("$.menuProducts[0].seq").exists())
            .andExpect(jsonPath("$.menuProducts[0].quantity").value(양념.getQuantity()))
            .andExpect(jsonPath("$.menuProducts[0].product.name").value(양념치킨.getName()))
            .andExpect(jsonPath("$.menuProducts[0].product.price").value(양념치킨.getPrice().longValue()));
    }

    @Test
    @DisplayName("메뉴를 목록을 가져온다")
    void list() throws Exception {
        mockMvc.perform(get("/api/menus"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$[2].id").value(반반치킨_메뉴.getId()))
            .andExpect(jsonPath("$[2].name").value(반반치킨_메뉴.getName()))
            .andExpect(jsonPath("$[2].price").value(반반치킨_메뉴.getPrice().longValue()));
    }
}
