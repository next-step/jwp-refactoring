package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@MockMvcTestConfig
@SuppressWarnings("NonAsciiCharacters")
public class MenuRestControllerTest {

    private static final String BASE_URL = "/api/menus";
    private static final long NOT_SAVED_ID = Long.MAX_VALUE;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("메뉴 생성 요청 성공")
    @Test
    void createMenuRequestSuccess() throws Exception {
        Menu menu = createMenu(30000);

        String content = objectMapper.writeValueAsString(menu);

        mockMvc.perform(post(BASE_URL).content(content)
                                      .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"));
    }

    @DisplayName("메뉴 생성 요청 실패 - 메뉴 가격이 음수이거나 설정되지 않음")
    @NullSource
    @ValueSource(ints = { -1000, -10000 })
    @ParameterizedTest
    void createMenuRequestFail01(Integer price) {
        Menu menu = createMenu(price);
        createMenuFail(menu);
    }

    @DisplayName("메뉴 생성 요청 실패 - 등록되지 않은 메뉴 그룹의 ID를 사용")
    @Test
    void createMenuRequestFail02() {
        Menu menu = createMenu(30000);
        menu.setMenuGroupId(NOT_SAVED_ID);
        createMenuFail(menu);
    }

    @DisplayName("메뉴 생성 요청 실패 - 메뉴 가격은 메뉴 상품 가격의 합보다 같거나 작아야 함")
    @ValueSource(ints = { 33000, 35000 })
    @ParameterizedTest
    void createMenuRequestFail03(int price) {

        // 테스트 케이스에서 사용된 메뉴 상품 가격
        // 후라이드: 16000원 * 1개
        // 양념: 16000원 * 1개
        // 총 32000원

        Menu menu = createMenu(price);
        createMenuFail(menu);
    }

    @DisplayName("메뉴 목록 조회 요청")
    @Test
    void findMenus() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL))
                                  .andDo(print())
                                  .andExpect(status().isOk())
                                  .andReturn();

        String response = result.getResponse().getContentAsString();
        List<Product> list = Arrays.asList(objectMapper.readValue(response, Product[].class));
        assertThat(list).hasSizeGreaterThanOrEqualTo(6); // default data size is 6
    }

    private void createMenuFail(Menu menu) {
        try {
            String content = objectMapper.writeValueAsString(menu);
            mockMvc.perform(post(BASE_URL).content(content)
                                          .contentType(MediaType.APPLICATION_JSON))
                   .andDo(print())
                   .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    public static Menu createMenu(Integer price) {
        // Product id 1: 후라이드, 2: 양념
        MenuProduct 후라이드 = new MenuProduct();
        후라이드.setProductId(1L);
        후라이드.setQuantity(1L);
        MenuProduct 양념 = new MenuProduct();
        양념.setProductId(2L);
        양념.setQuantity(1L);

        List<MenuProduct> menuProducts = Arrays.asList(후라이드, 양념);

        Menu 후라이드양념두마리세트 = new Menu();
        후라이드양념두마리세트.setName("후라이드양념두마리세트");
        if (price != null) {
            후라이드양념두마리세트.setPrice(new BigDecimal(price));
        }
        후라이드양념두마리세트.setMenuGroupId(1L); // 두마리세트 ID
        후라이드양념두마리세트.setMenuProducts(menuProducts);
        return 후라이드양념두마리세트;
    }
}
