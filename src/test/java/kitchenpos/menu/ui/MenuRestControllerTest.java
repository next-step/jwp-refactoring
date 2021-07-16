package kitchenpos.menu.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import kitchenpos.menu.domain.entity.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.entity.MenuProduct;
import kitchenpos.menu.domain.value.Price;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.entity.Product;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
@AutoConfigureMockMvc
class MenuRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    MenuRestController menuRestController;

    @Autowired
    ObjectMapper objectMapper;

    MenuGroup 메뉴그룹_한마리메뉴;
    Product 프로덕트_후라이드치킨;
    MenuProduct 메뉴프로덕트_후라이드치킨_후라이드치킨;
    Menu 메뉴_후라이드;

    MenuGroupRequest 메뉴그룹_한마리메뉴_리퀘스트;
    ProductRequest 프로덕트_후라이드치킨_리퀘스트;
    MenuProductRequest 메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트;
    MenuRequest 메뉴_후라이드_리퀘스트;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuRestController)
            .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
            .alwaysDo(print())
            .build();

        메뉴그룹_한마리메뉴 = new MenuGroup(1L, "한마리메뉴");
        프로덕트_후라이드치킨 = new Product(1L, "후라이드치킨", BigDecimal.valueOf(18000));
        메뉴프로덕트_후라이드치킨_후라이드치킨 = new MenuProduct(1L, 프로덕트_후라이드치킨.getId(), 1);
        메뉴_후라이드 = Menu.of("후라이드", Price.of(BigDecimal.valueOf(18000)), 메뉴그룹_한마리메뉴.getId(),
            Arrays.asList(메뉴프로덕트_후라이드치킨_후라이드치킨));

        메뉴그룹_한마리메뉴_리퀘스트 = new MenuGroupRequest(1L, "한마리메뉴");
        프로덕트_후라이드치킨_리퀘스트 = new ProductRequest(1L, "후라이드치킨", BigDecimal.valueOf(18000));
        메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트 = new MenuProductRequest(1L, 1L, 프로덕트_후라이드치킨_리퀘스트.getId(), 1);
        메뉴_후라이드_리퀘스트 = new MenuRequest("후라이드", BigDecimal.valueOf(16000), 메뉴그룹_한마리메뉴_리퀘스트.getId(),
            Arrays.asList(메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트));
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(메뉴_후라이드_리퀘스트);

        //when
        mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("메뉴가격이 0보다 작거나 비어있는경우 메뉴 생성을 실패한다.")
    void create_with_exception_when_price_smaller_than_zero_or_null() throws Exception {
        //given
        메뉴_후라이드_리퀘스트 = new MenuRequest("후라이드", BigDecimal.valueOf(-1), 메뉴그룹_한마리메뉴_리퀘스트.getId(),
            Arrays.asList(메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트));

        //when && then
        메뉴_생성_요청_실패_re();
    }

    @Test
    @DisplayName("메뉴 그룹이 없는 경우 메뉴 생성을 실패한다.")
    void create_with_exception_when_menu_group_is_null_re() throws Exception {
        //given
        메뉴_후라이드_리퀘스트 = new MenuRequest("후라이드", BigDecimal.valueOf(-1), null,
            Arrays.asList(메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트));

        //when && then
        메뉴_생성_요청_실패_re();
    }

    @Test
    @DisplayName("메뉴의 가격이 포함된 상품의 가격합보다 큰 경우 메뉴 생성을 실패한다.")
    void create_with_exception_when_menu_price_greater_than_sum_of_product_re() throws Exception {
        //given
        메뉴_후라이드_리퀘스트 = new MenuRequest("후라이드", BigDecimal.valueOf(9999999), 메뉴그룹_한마리메뉴_리퀘스트.getId(),
            Arrays.asList(메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트));

        //when && then
        메뉴_생성_요청_실패_re();
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list_re() throws Exception {
        //when && then
        mockMvc.perform(get("/api/menus"))
            .andExpect(status().isOk());
    }

    private void 메뉴_생성_요청_실패_re() throws Exception {
        String requestBody = objectMapper.writeValueAsString(메뉴_후라이드_리퀘스트);
        mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isBadRequest());
    }

}