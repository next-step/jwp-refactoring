package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;

class MenuGroupRestControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴그룹을 등록한다")
    @Test
    void group1() throws Exception {
        MenuGroup group = new MenuGroup("이벤트메뉴");

        MvcResult result = mockMvc.perform(post("/api/menu-groups")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(group)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(group.getName()))
            .andReturn();

        assertThat(menuGroupRepository.findById(getId(result))).isNotEmpty();
    }

    @DisplayName("전체 메뉴그룹을 조회한다")
    @Test
    void group2() throws Exception {
        mockMvc.perform(get("/api/menu-groups"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..id").exists())
            .andExpect(jsonPath("$..name").exists())
        ;
    }

    private Long getId(MvcResult result) throws
        com.fasterxml.jackson.core.JsonProcessingException,
        UnsupportedEncodingException {
        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, MenuGroup.class).getId();
    }

}
