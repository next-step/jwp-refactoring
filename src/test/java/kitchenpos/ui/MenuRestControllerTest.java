package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.MenuGroupServiceTest.메뉴_그룹;
import static kitchenpos.domain.MenuProductTest.메뉴_상품;
import static kitchenpos.domain.MenuTest.메뉴;
import static kitchenpos.domain.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("메뉴 ui 테스트")
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() throws Exception {
        //given:
        final Menu 메뉴 = 메뉴("자메이카 통다리 1인 세트",
                BigDecimal.ONE,
                menuGroupRepository.save(메뉴_그룹("추천 메뉴")).getId(),
                Arrays.asList(
                        메뉴_상품(productRepository.save(상품("통다리", BigDecimal.ONE)), 5),
                        메뉴_상품(productRepository.save(상품("콜라", BigDecimal.ONE)), 1)));
        //when:
        final Menu 저장된_메뉴 = mapper.readValue(
                mockMvc.perform(post("/api/menus")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(메뉴)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(), Menu.class);
        //then:
        assertThat(저장된_메뉴).isEqualTo(메뉴);
    }

    @DisplayName("목록 조회 성공")
    @Test
    void 목록_조회_성공() throws Exception {
        //when:
        final List<Menu> 메뉴_목록 = Arrays.asList(mapper.readValue(
                mockMvc.perform(get("/api/menus")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), Menu[].class));
        //then:
        assertThat(메뉴_목록).isNotEmpty();
    }
}
