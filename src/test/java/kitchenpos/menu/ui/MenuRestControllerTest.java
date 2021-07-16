package kitchenpos.menu.ui;

import static kitchenpos.menugroup.domain.MenuGroupTest.*;
import static kitchenpos.menu.domain.MenuTest.*;
import static kitchenpos.product.domain.ProductTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.utils.IntegrationTest;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("메뉴 통합 테스트")
class MenuRestControllerTest extends IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    MenuProductRequest 양념;
    MenuProductRequest 간장;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        양념 = new MenuProductRequest(양념치킨.getId(), 1);
        간장 = new MenuProductRequest(간장치킨.getId(), 1);
    }

    @Test
    @DisplayName("메뉴를 생성한다")
    void create() throws Exception {
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
            .andExpect(jsonPath("$.menuGroupId").value(두마리메뉴.getId()))
            .andExpect(jsonPath("$.menuProducts[0].seq").exists())
            .andExpect(jsonPath("$.menuProducts[0].quantity").value(양념.getQuantity()))
            .andExpect(jsonPath("$.menuProducts[0].productId").value(양념치킨.getId()));
    }

    @Test
    @DisplayName("메뉴 생성이 실패한다 - 가격이 null 이거나, 0보다 작을 경우")
    void create_failed_1() throws Exception {
        BigDecimal invalidPrice = BigDecimal.valueOf(-1);
        MenuRequest request = new MenuRequest(
            "양념+간장", invalidPrice, 두마리메뉴.getId(), Arrays.asList(양념, 간장));
        mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 생성이 실패한다 - 메뉴 그룹 ID가 존재하지 않을 경우")
    void create_failed_2() throws Exception {
        Long invalidId = -1L;
        MenuRequest request = new MenuRequest(
            "양념+간장", BigDecimal.valueOf(33000), invalidId, Arrays.asList(양념, 간장));
        mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 생성이 실패한다 - 제품이 존재하지 않을 경우")
    void create_failed_3() throws Exception {
        Long invalidId = -1L;
        양념.setProductId(invalidId);
        MenuRequest request = new MenuRequest(
            "양념+간장", BigDecimal.valueOf(33000), 두마리메뉴.getId(), Arrays.asList(양념, 간장));
        mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 생성이 실패한다 - 제품 가격의 합을 초과할 때")
    void create_failed_4() throws Exception {
        BigDecimal exceedPrice = BigDecimal.valueOf(33001);
        MenuRequest request = new MenuRequest(
            "양념+간장", exceedPrice, 두마리메뉴.getId(), Arrays.asList(양념, 간장));
        mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isConflict());
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
            .andExpect(jsonPath("$[2].price").value(반반치킨_메뉴.getPrice().longValue()))
            .andExpect(jsonPath("$[2].menuGroupId").value(2));
    }
}
