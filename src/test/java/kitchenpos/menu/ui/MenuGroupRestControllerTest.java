package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.domain.MenuGroup;
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

import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹_추천_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("메뉴 그룹 ui 테스트")
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() throws Exception {
        //given:
        final MenuGroup 메뉴_그룹 = 메뉴_그룹_추천_메뉴();
        //when:
        final MenuGroup 저장된_메뉴_그룹 = mapper.readValue(mockMvc.perform(
                        post("/api/menu-groups")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(메뉴_그룹)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(), MenuGroup.class);
        //then:
        assertThat(저장된_메뉴_그룹).isEqualTo(메뉴_그룹);
    }

    @DisplayName("목록 조회 성공")
    @Test
    void 목록_조회_성공() throws Exception {
        //when:
        final List<MenuGroup> 메뉴_그룹_목록 = Arrays.asList(mapper.readValue(
                mockMvc.perform(get("/api/menu-groups")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), MenuGroup[].class));
        //then:
        assertThat(메뉴_그룹_목록).isNotEmpty();
    }
}
