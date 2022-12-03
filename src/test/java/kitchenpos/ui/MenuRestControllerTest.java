package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MenuRestController.class)
@DisplayName("MenuRestController 클래스 테스트")
public class MenuRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MenuRestController menuRestController;

    @Nested
    @DisplayName("POST /api/menus 테스트")
    public class PostMethod {
        @Test
        @DisplayName("성공적으로 메류를 등록하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final Menu menu = setup();

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/menus")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(menu))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

            // then
            final Menu menuResponse =
                    objectMapper.readValue(response.getContentAsString(), Menu.class);
            assertThat(menuResponse.getName()).isEqualTo(menu.getName());
        }

        private Menu setup() {
            final Menu menu = new Menu();
            menu.setName("test menu");
            menu.setPrice(BigDecimal.valueOf(0));
            Mockito.when(menuRestController.create(Mockito.any())).thenReturn(ResponseEntity.ok(menu));
            return menu;
        }

        @Test
        @DisplayName("가격을 입력하지 않아서 메류를 등록하지 못하면 400 상태 코드를 응답받는다")
        public void badRequestPriceNull() throws Exception {
            // given
            final Menu menu = setupBadRequestPriceNull();

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/menus")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(menu))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private Menu setupBadRequestPriceNull() {
            final Menu menu = new Menu();
            menu.setName("test menu");
            menu.setPrice(null);
            Mockito.when(menuRestController.create(Mockito.any())).thenReturn(ResponseEntity.badRequest().build());
            return menu;
        }

        @Test
        @DisplayName("가격을 음수로 입력해서 메류를 등록하지 못하면 400 상태 코드를 응답받는다")
        public void badRequestPriceNegative() throws Exception {
            // given
            final Menu menu = setupBadRequestPriceNegative();

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/menus")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(menu))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private Menu setupBadRequestPriceNegative() {
            final Menu menu = new Menu();
            menu.setName("test menu");
            menu.setPrice(BigDecimal.valueOf(-999));
            Mockito.when(menuRestController.create(Mockito.any())).thenReturn(ResponseEntity.badRequest().build());
            return menu;
        }

        @Test
        @DisplayName("지정한 메뉴 가격이 포함하고 있는 상품의 가격 총합보다 커서 메류를 등록하지 못하면 400 상태 코드를 응답받는다")
        public void badRequestPriceMoreThanSumOfProductPrice() throws Exception {
            // given
            final Menu menu = setupBadRequestPriceMoreThanSumOfProductPrice();

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/menus")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(menu))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private Menu setupBadRequestPriceMoreThanSumOfProductPrice() {
            final Menu menu = new Menu();
            menu.setName("test menu");
            menu.setPrice(BigDecimal.valueOf(1500));
            Mockito.when(menuRestController.create(Mockito.any())).thenReturn(ResponseEntity.badRequest().build());
            return menu;
        }
    }

    @Nested
    @DisplayName("GET /api/menus 테스트")
    public class GetList {
        @Test
        @DisplayName("성공적으로 메뉴 목록을 조회하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            setup();

            // when
            MockHttpServletResponse response = mockMvc.perform(get("/api/menus")
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        }

        private void setup() {
            List<Menu> menus = Arrays.asList(new Menu(), new Menu(), new Menu());
            Mockito.when(menuRestController.list()).thenReturn(ResponseEntity.ok(menus));
        }
    }
}
