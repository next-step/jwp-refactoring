package kitchenpos.menu.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MenuRestControllerTest {
    private MockMvc mockMvc;

    @Autowired
    MenuRestController menuRestController;

    @Autowired
    ObjectMapper objectMapper;

    Menu 메뉴;
    BigDecimal 프로덕트의_합보다_큰_가격 = BigDecimal.valueOf(9999999);

    @BeforeEach
    void setUp() {
        System.out.println(">>>>>>>>>>>>>>>>>>setup");
        mockMvc = MockMvcBuilders.standaloneSetup(menuRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        메뉴 = getMenu();
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(메뉴);

        //when
        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create_re() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(메뉴);

        //when
        mockMvc.perform(post("/api/menus_re")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @TestFactory
    @DisplayName("메뉴 실패 테스트")
    Stream<DynamicTest> create_with_exception() {
        return Stream.of(
                dynamicTest("메뉴가격이 0보다 작은 경우", () -> {
                    //given
                    메뉴 = getMenu();
                    메뉴.setPrice(BigDecimal.valueOf(-1));

                    //when && then
                    메뉴_생성_요청_실패();
                }),
                dynamicTest("메뉴가격이 비어있는 경우", () -> {
                    //given
                    메뉴 = getMenu();
                    메뉴.setPrice(null);

                    //when && then
                    메뉴_생성_요청_실패();
                }),
                dynamicTest("메뉴 그룹이 없는 경우", () -> {
                    //given
                    메뉴 = getMenu();
                    메뉴.setMenuGroupId(null);

                    //when && then
                    메뉴_생성_요청_실패();
                }),
                dynamicTest("메뉴의 가격이 포함된 상품의 가격합보다 큰 경우", () -> {
                    //given
                    메뉴 = getMenu();
                    메뉴.setPrice(프로덕트의_합보다_큰_가격);

                    //when && then
                    메뉴_생성_요청_실패();
                })
        );
    }

    @TestFactory
    @DisplayName("메뉴 실패 테스트")
    Stream<DynamicTest> create_with_exception_re() {
        return Stream.of(
                dynamicTest("메뉴가격이 0보다 작은 경우", () -> {
                    //given
                    메뉴 = getMenu();
                    메뉴.setPrice(BigDecimal.valueOf(-1));

                    //when && then
                    메뉴_생성_요청_실패_re();
                }),
                dynamicTest("메뉴가격이 비어있는 경우", () -> {
                    //given
                    메뉴 = getMenu();
                    메뉴.setPrice(null);

                    //when && then
                    메뉴_생성_요청_실패_re();
                }),
                dynamicTest("메뉴 그룹이 없는 경우", () -> {
                    //given
                    메뉴 = getMenu();
                    메뉴.setMenuGroupId(null);

                    //when && then
                    메뉴_생성_요청_실패_re();
                }),
                dynamicTest("메뉴의 가격이 포함된 상품의 가격합보다 큰 경우", () -> {
                    //given
                    메뉴 = getMenu();
                    메뉴.setPrice(프로덕트의_합보다_큰_가격);

                    //when && then
                    메뉴_생성_요청_실패_re();
                })
        );
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() throws Exception {
        //when && then
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list_re() throws Exception {
        //when && then
        mockMvc.perform(get("/api/menus_re"))
                .andExpect(status().isOk());
    }

    private void 메뉴_생성_요청_실패() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(메뉴);
        try {
            mockMvc.perform(post("/api/menus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private void 메뉴_생성_요청_실패_re() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(메뉴);
        try {
            mockMvc.perform(post("/api/menus_re")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            assertThat(e.getCause())
                    .isInstanceOfAny(IllegalArgumentException.class, InvalidDataAccessApiUsageException.class);

        }
    }

    private Menu getMenu() {
        MenuProduct 메뉴프로덕트 = new MenuProduct();
        메뉴프로덕트.setMenuId(1L);
        메뉴프로덕트.setProductId(1L);
        메뉴프로덕트.setQuantity(1);

        메뉴 = new Menu();
        메뉴.setId(1L);
        메뉴.setName("후라이드+후라이드");
        메뉴.setPrice(BigDecimal.valueOf(13000));
        메뉴.setMenuGroupId(1L);
        메뉴.setMenuProducts(Arrays.asList(메뉴프로덕트));
        return 메뉴;
    }
}