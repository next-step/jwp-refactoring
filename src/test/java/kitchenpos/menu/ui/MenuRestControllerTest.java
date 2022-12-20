package kitchenpos.menu.ui;

import kitchenpos.ControllerTest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MenuRestController 테스트")
@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest extends ControllerTest {
    @MockBean
    private MenuService menuService;

    private MenuGroup 양식;
    private Menu 양식_세트;
    private Product 스파게티;
    private Product 스테이크;
    private Product 콜라;
    private List<MenuProductRequest> menuProducts;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식_세트", 40000, 양식);

        스테이크 = new Product("스테이크", 20000);
        스파게티 = new Product("스파게티", 15000);
        콜라 = new Product("콜라", 2500);

        ReflectionTestUtils.setField(양식, "id", 1L);
        ReflectionTestUtils.setField(양식_세트, "id", 1L);

        ReflectionTestUtils.setField(스테이크, "id", 1L);
        ReflectionTestUtils.setField(스파게티, "id", 2L);
        ReflectionTestUtils.setField(콜라, "id", 3L);

        양식_세트.create(Arrays.asList(
                new MenuProduct(양식_세트, 스테이크, 1L),
                new MenuProduct(양식_세트, 스파게티, 1L),
                new MenuProduct(양식_세트, 콜라, 2L)));

        menuProducts = Arrays.asList(
                new MenuProductRequest(스테이크.getId(), 1L),
                new MenuProductRequest(스파게티.getId(), 1L),
                new MenuProductRequest(콜라.getId(), 2L));
    }

    @DisplayName("메뉴 등록에 실패한다.")
    @Test
    void 메뉴_등록에_실패한다() throws Exception {
        given(menuService.create(any(MenuRequest.class))).willThrow(IllegalArgumentException.class);

        MenuRequest menuRequest = new MenuRequest(양식_세트.getName(), 양식_세트.getPrice().intValue(), 양식.getId(), menuProducts);

        webMvc.perform(post("/api/menus")
                        .content(objectMapper.writeValueAsString(menuRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("메뉴 등록에 성공한다.")
    @Test
    void 메뉴_등록에_성공한다() throws Exception {
        given(menuService.create(any(MenuRequest.class))).willReturn(new MenuResponse(양식_세트));

        MenuRequest menuRequest = new MenuRequest(양식_세트.getName(), 양식_세트.getPrice().intValue(), 양식.getId(), menuProducts);

        webMvc.perform(post("/api/menus")
                        .content(objectMapper.writeValueAsString(menuRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(양식_세트.getId().intValue())))
                .andExpect(jsonPath("$.name", is(양식_세트.getName())))
                .andExpect(jsonPath("$.price", is(양식_세트.getPrice().intValue())))
                .andExpect(jsonPath("$.menuGroup.id", is(양식_세트.getId().intValue())))
                .andExpect(jsonPath("$.menuProducts", hasSize(3)));
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록을_조회한다() throws Exception {
        given(menuService.list()).willReturn(Arrays.asList(new MenuResponse(양식_세트)));

        webMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
