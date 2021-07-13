package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    private MenuRequest menuRequest;
    MenuProductRequest 후라이드;
    MenuProductRequest 양념치킨;

    @BeforeEach
    public void setUp() {
        후라이드 =  new MenuProductRequest( 1L, 1);
        양념치킨 =  new MenuProductRequest( 1L, 1);
        List<MenuProductRequest> 두마리치킨 = Arrays.asList(후라이드, 양념치킨);
        menuRequest = new MenuRequest("두마리특급세일", BigDecimal.valueOf(10000), 1L, 두마리치킨);
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void createTest() {

        //when
        MenuResponse menuResponse = menuService.create(menuRequest);

        //then
        assertThat(menuResponse.getName()).isEqualTo(menuRequest.getName());
        assertThat(menuResponse.getMenuGroupId()).isEqualTo(menuRequest.getMenuGroupId());
        assertThat(menuResponse.getPrice()).isEqualTo(menuRequest.getPrice());
        assertThat(menuResponse.getMenuProducts().stream().map(MenuProductResponse::getProductId)).contains(후라이드.getProductId());
        assertThat(menuResponse.getMenuProducts().stream().map(MenuProductResponse::getProductId)).contains(양념치킨.getProductId());
    }

    @DisplayName("메뉴를 조회할 수 있다")
    @Test
    void listTest() {
        //given
        menuService.create(menuRequest);

        //when
        List<MenuResponse> menuResponse = menuService.list();

        //then
        List<String> menuNames = menuResponse.stream().map(MenuResponse::getName).collect(Collectors.toList());
        assertThat(menuNames).contains(menuRequest.getName());
    }


}