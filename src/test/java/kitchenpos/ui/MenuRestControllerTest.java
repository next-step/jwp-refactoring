package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MenuRestControllerTest {

    private Menu 마늘치킨;
    private Menu 허니치킨;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        마늘치킨 = new Menu();
        마늘치킨.setId(7L);
        마늘치킨.setName("마늘치킨");
        마늘치킨.setPrice(BigDecimal.valueOf(18000));
        마늘치킨.setMenuGroupId(4L);

        허니치킨 = new Menu();
        허니치킨.setId(8L);
        허니치킨.setName("허니치킨");
        허니치킨.setPrice(BigDecimal.valueOf(20000));
        허니치킨.setMenuGroupId(4L);
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() {
        // given
        given(menuService.create(any())).willReturn(마늘치킨);

        // when
        Menu actual = testRestTemplate.postForObject("/api/menus", 마늘치킨, Menu.class);

        // then
        assertThat(actual.getName()).isEqualTo(마늘치킨.getName());
        assertThat(actual.getPrice()).isEqualTo(마늘치킨.getPrice());
    }

    @DisplayName("등록한 메뉴목록을 조회한다")
    @Test
    void findAll() {
        // given
        given(menuService.list()).willReturn(Arrays.asList(마늘치킨, 허니치킨));

        // when
        Menu[] menus = testRestTemplate.getForObject("/api/menus", Menu[].class);

        // then
        List<String> actual = Arrays.stream(menus)
                .map(Menu::getName)
                .collect(Collectors.toList());
        assertThat(actual).containsAll(Arrays.asList("마늘치킨", "허니치킨"));
    }
}