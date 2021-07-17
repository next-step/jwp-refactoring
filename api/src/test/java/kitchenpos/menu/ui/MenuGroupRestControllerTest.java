package kitchenpos.menu.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("메뉴 그룹 관련 기능 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MenuGroupRestControllerTest {
    public static final String MENU_GROUP_URI = "/api/menu-groups";
    private MockMvc mockMvc;
    private MenuGroup menuGroup1;
    private MenuGroup menuGroup2;

    private MenuGroupResponse menuGroupResponse1;
    private MenuGroupResponse menuGroupResponse2;

    @MockBean
    private MenuGroupService menuGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MenuGroupRestController menuGroupRestController;

    @BeforeEach
    void setUp() {
        setUpMockMvc();

        menuGroup1 = new MenuGroup(1L, "반반시리즈");

        menuGroup2 = new MenuGroup(2L, "허니시리즈");

        menuGroupResponse1 = new MenuGroupResponse(menuGroup1);
        menuGroupResponse2 = new MenuGroupResponse(menuGroup2);
    }

    @DisplayName("메뉴 그룹 등록한다.")
    @Test
    void create() throws Exception {
        given(menuGroupService.create(any())).willReturn(menuGroupResponse1);

        final ResultActions actions = 메뉴_그룹_등록_요청();

        메뉴_그룹_등록됨(actions);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() throws Exception {
        given(menuGroupService.list()).willReturn(Arrays.asList(menuGroupResponse1, menuGroupResponse2));

        final ResultActions actions =  메뉴_그룹_목록_조회();

        메뉴_그룹_목록_조회됨(actions);
    }

    public String toJson(MenuGroup product) throws JsonProcessingException {
        return objectMapper.writeValueAsString(product);
    }

    private void setUpMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }

    private void 메뉴_그룹_등록됨(ResultActions actions) throws Exception {
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/menu-groups/1"))
                .andExpect(content().string(containsString("반반시리즈")));
    }

    private ResultActions 메뉴_그룹_등록_요청() throws Exception {
        return mockMvc.perform(post(MENU_GROUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(menuGroup1)));
    }

    private void 메뉴_그룹_목록_조회됨(ResultActions actions) throws Exception{
        actions.andExpect(status().isOk())
                .andExpect(content().string(containsString("반반시리즈")))
                .andExpect(content().string(containsString("허니시리즈")));
    }

    private ResultActions 메뉴_그룹_목록_조회() throws Exception{
        return  mockMvc.perform(get(MENU_GROUP_URI)
                .contentType(MediaType.APPLICATION_JSON));
    }
}