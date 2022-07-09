package kitchenpos.menu;

import kitchenpos.application.menu.MenuService;
import kitchenpos.application.menu.MenuValidator;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuProductResponse;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.exception.product.PriceException;
import kitchenpos.repository.menu.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.factory.MenuFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuValidator menuValidator;

    @InjectMocks
    MenuService menuService;

    MenuProduct 양념치킨한마리;
    MenuProduct 간장치킨한마리;

    MenuGroup 한마리메뉴;
    Product 양념치킨;
    Product 간장치킨;
    Menu 치킨;

    @Test
    @DisplayName("메뉴를 생성한다 (Happy Path)")
    void create() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨.getId());
        간장치킨한마리 = 메뉴_상품_생성(간장치킨.getId());
        MenuProductRequest 양념치킨한마리Request = new MenuProductRequest(양념치킨.getId(), 1);
        MenuProductRequest 간장치킨한마리Request = new MenuProductRequest(간장치킨.getId(), 1);
        MenuRequest 치킨Request = new MenuRequest("치킨", new BigDecimal(30000),
                                                    한마리메뉴.getId(), Arrays.asList(양념치킨한마리Request, 간장치킨한마리Request));
        치킨 = new Menu(1L, "치킨", new BigDecimal(30000), 한마리메뉴.getId(), Arrays.asList(양념치킨한마리, 간장치킨한마리));
        given(menuRepository.save(any(Menu.class))).willReturn(치킨);
        willDoNothing().given(menuValidator).validation(any());

        //when
        MenuResponse menuResponse = menuService.create(치킨Request);

        assertAll(() -> {
            assertThat(menuResponse.getId()).isEqualTo(치킨.getId());
            assertThat(menuResponse.getName()).isEqualTo(치킨.getName());
            assertThat(menuResponse.getMenuGroupId()).isEqualTo(한마리메뉴.getId());
            assertThat(menuResponse.getMenuProductResponses().stream()
                    .map(MenuProductResponse::getProductId)
                    .collect(Collectors.toList())).containsExactlyInAnyOrderElementsOf(Arrays.asList(양념치킨한마리.getProductId(), 간장치킨한마리.getProductId()));
        });
    }

    @Test
    @DisplayName("유효하지 않은 금액(0원 미만)으로 메뉴 생성은 불가능하다.")
    void createInvalidPrice() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨.getId());
        간장치킨한마리 = 메뉴_상품_생성(간장치킨.getId());
        MenuProductRequest 양념치킨한마리Request = new MenuProductRequest(양념치킨.getId(), 1);
        MenuProductRequest 간장치킨한마리Request = new MenuProductRequest(간장치킨.getId(), 1);
        MenuRequest 치킨Request = new MenuRequest("치킨", new BigDecimal(-1),
                한마리메뉴.getId(), Arrays.asList(양념치킨한마리Request, 간장치킨한마리Request));
        willDoNothing().given(menuValidator).validation(any());

        //then
        assertThatThrownBy(() -> {
            menuService.create(치킨Request);
        }).isInstanceOf(PriceException.class)
            .hasMessageContaining(PriceException.INVALID_PRICE_MSG);
    }

    @Test
    void list() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨.getId());
        간장치킨한마리 = 메뉴_상품_생성(간장치킨.getId());
        치킨 = new Menu(1L, "치킨", new BigDecimal(30000), 한마리메뉴.getId(), Arrays.asList(양념치킨한마리, 간장치킨한마리));
        given(menuRepository.findAll()).willReturn(Arrays.asList(치킨));

        //when
        List<MenuResponse> menus = menuService.list();

        //then
        assertAll(() -> {
            assertThat(menus.stream().map(MenuResponse::getId)
                    .collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Arrays.asList(치킨.getId()));
            assertThat(menus.stream().map(MenuResponse::getPrice)
                    .collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Arrays.asList(치킨.getPrice()));
        });
    }
}