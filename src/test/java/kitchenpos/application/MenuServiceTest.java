package kitchenpos.application;

import kitchenpos.AcceptanceTest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class MenuServiceTest extends AcceptanceTest {
    @Autowired
    private MenuService menuService;

    private List<MenuProductRequest> menuProductRequests;
    private MenuProductRequest menuProductRequest_후라이드;
    private MenuProductRequest menuProductRequest_양념;
    private String 새로운_메뉴_NAME;
    private BigDecimal 새로운_메뉴_PRICE;
    private Long 새로운_메뉴_MENU_GROUP_ID;

    @BeforeEach
    public void setUp() {
        menuProductRequest_후라이드 = new MenuProductRequest(1L, 1);
        menuProductRequest_양념 = new MenuProductRequest(2L, 1);
        menuProductRequests = Arrays.asList(menuProductRequest_후라이드, menuProductRequest_양념);
        새로운_메뉴_NAME = "후라이드+양념";
        새로운_메뉴_PRICE = BigDecimal.valueOf(25000);
        새로운_메뉴_MENU_GROUP_ID = 1L;
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void create() {
        //given
        MenuRequest menuRequest = new MenuRequest(새로운_메뉴_NAME, 새로운_메뉴_PRICE, 새로운_메뉴_MENU_GROUP_ID, menuProductRequests);

        //when
        MenuResponse result = menuService.create(menuRequest);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(새로운_메뉴_NAME);
        assertThat(result.getPrice()).isEqualByComparingTo(새로운_메뉴_PRICE);
        assertThat(result.getMenuGroupId()).isEqualTo(1L);
        assertThat(result.getMenuProducts().size()).isEqualTo(2);
        assertThat(result.getMenuProducts().get(0).getMenuId()).isEqualTo(result.getId());
        assertThat(result.getMenuProducts().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getMenuProducts().get(1).getMenuId()).isEqualTo(result.getId());
        assertThat(result.getMenuProducts().get(1).getProductId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("메뉴 등록 시 가격은 필수정보이다.")
    void createMenuPriceNull() {
        // given
        MenuRequest menuRequest = new MenuRequest(새로운_메뉴_NAME, null, 새로운_메뉴_MENU_GROUP_ID, menuProductRequests);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            MenuResponse response = menuService.create(menuRequest);
        }).withMessageMatching("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("메뉴 그룹이 등록되어 있어야 한다.")
    void createNotExistMenuGroup() {
        //given
        MenuRequest menuRequest = new MenuRequest(새로운_메뉴_NAME, 새로운_메뉴_PRICE, 0L, menuProductRequests);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            MenuResponse response = menuService.create(menuRequest);
        });
    }

    @Test
    @DisplayName("상품이 등록되어 있어야 한다.")
    void createNotExistProduct() {
        //given
        MenuProductRequest notExistProduct = new MenuProductRequest(0L, 1);
        MenuRequest menuRequest = new MenuRequest(새로운_메뉴_NAME, 새로운_메뉴_PRICE, 새로운_메뉴_MENU_GROUP_ID, Arrays.asList(notExistProduct));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            MenuResponse response = menuService.create(menuRequest);
        }).withMessageMatching("등록되지 않은 상품 입니다.");
    }

    @Test
    @DisplayName("메뉴 가격이 메뉴에 속하는 상품 가격의 합보다 크지 않아야 한다.")
    void createPriceGreaterThanSum() {
        // given
        BigDecimal greaterThanSum = 새로운_메뉴_PRICE.add(BigDecimal.valueOf(10000));
        MenuRequest menuRequest = new MenuRequest(새로운_메뉴_NAME, greaterThanSum, 새로운_메뉴_MENU_GROUP_ID, menuProductRequests);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            MenuResponse response = menuService.create(menuRequest);
        }).withMessageMatching("메뉴 가격이 속한 상품 가격들의 합보다 비쌉니다.");
    }

    @Test
    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    void findAllMenus() {
        // when
        List<MenuResponse> menuResponses = menuService.list();

        // then
        assertThat(menuResponses).isNotEmpty();
        assertThat(menuResponses.stream()
                .map(MenuResponse::getName)
                .collect(Collectors.toList())).containsAll(Arrays.asList("후라이드치킨", "양념치킨", "반반치킨", "간장치킨", "순살치킨"));
    }
}
