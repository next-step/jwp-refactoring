package kitchenpos.menugroup.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("메뉴그룹 관리 기능 - SpringBootTest")
@SpringBootTest
class MenuGroupRestControllerSpringBootTest extends MockMvcControllerTest {
    public static final String DEFAULT_REQUEST_URL = "/api/menu-groups";
    @Autowired
    private MenuGroupRestController menuGroupRestController;

    @Override
    protected Object controller() {
        return this.menuGroupRestController;
    }

    @Test
    @DisplayName("메뉴그룹을 등록할 수 있다.")
    void save_menuGroup() throws Exception {
        // then
        mockMvc.perform(post( DEFAULT_REQUEST_URL)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(new MenuGroupRequest("A"))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value("A"))
        ;
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회할 수 있다.")
    void retrieve_menuGroupList() throws Exception {
        // then
        mockMvc.perform(get(DEFAULT_REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(1))
                .andExpect(jsonPath("[0].name").value("두마리메뉴"))
                .andExpect(jsonPath("[3].id").value(4))
                .andExpect(jsonPath("[3].name").value("신메뉴"))
        ;
    }
}
