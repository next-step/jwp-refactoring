package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다")
    @Test
    void testCreate() {
        // given
        Product 볶음짜장면 = new Product(1L, "볶음짜장면", 8000);
        Product 삼선짬뽕 = new Product(2L, "삼선짬뽕", 8000);

        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        MenuGroup menuGroup = new MenuGroup(1L, "식사류");
        MenuRequest menuRequest = new MenuRequest("집밥이최고", 16000L, menuGroup.getId(), menuProductRequests);
        menuProductRequests.add(new MenuProductRequest(볶음짜장면.getId(), 1));
        menuProductRequests.add(new MenuProductRequest(삼선짬뽕.getId(), 1));

        List<MenuProduct> menuProducts = new ArrayList<>();
        Menu expectedMenu = new Menu(1L, "집밥이최고", 16000, new MenuGroup(), menuProducts);
        menuProducts.add(new MenuProduct(1L, expectedMenu, 볶음짜장면, 1));
        menuProducts.add(new MenuProduct(2L, expectedMenu, 삼선짬뽕, 1));

        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));
        given(productRepository.findById(anyLong())).willReturn(Optional.of(볶음짜장면), Optional.of(삼선짬뽕));
        given(menuRepository.save(any(Menu.class))).willReturn(expectedMenu);

        // when
        MenuResponse menu = menuService.create(menuRequest);

        // then
        assertThat(menu).isEqualTo(MenuResponse.of(expectedMenu));
    }

    @DisplayName("등록된 메뉴 그룹이 포함되어야 한다")
    @Test
    void givenNonMenuGroupThenThrowException() {
        // given
        Product 볶음짜장면 = new Product(1L, "볶음짜장면", 8000);
        Product 삼선짬뽕 = new Product(2L, "삼선짬뽕", 8000);

        List<MenuProductRequest> menuProducts = new ArrayList<>();
        MenuGroup menuGroup = new MenuGroup(1L, "식사류");
        MenuRequest menuRequest = new MenuRequest(1L, "대표메뉴", 16000L, menuGroup.getId(), menuProducts);
        Menu expectedMenu = new Menu();
        menuProducts.add(new MenuProductRequest(1L, expectedMenu.getId(), 볶음짜장면.getId(), 1));
        menuProducts.add(new MenuProductRequest(2L, expectedMenu.getId(), 삼선짬뽕.getId(), 1));

        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        ThrowableAssert.ThrowingCallable callable = () -> menuService.create(menuRequest);

        // then
        assertThatThrownBy(callable)
                .isInstanceOf(MenuGroupNotFoundException.class);
    }

    @DisplayName("등록된 상품이 포함되어야 한다")
    @Test
    void givenNonProductThenThrowException() {
        // given
        Product 볶음짜장면 = new Product(1L, "볶음짜장면", 8000);
        Product 삼선짬뽕 = new Product(2L, "삼선짬뽕", 8000);

        List<MenuProductRequest> menuProducts = new ArrayList<>();
        MenuGroup menuGroup = new MenuGroup(1L, "식사류");
        MenuRequest menuRequest = new MenuRequest(1L, "대표메뉴", 16000L, menuGroup.getId(), menuProducts);
        Menu expectedMenu = new Menu();
        menuProducts.add(new MenuProductRequest(1L, expectedMenu.getId(), 볶음짜장면.getId(), 1));
        menuProducts.add(new MenuProductRequest(2L, expectedMenu.getId(), 삼선짬뽕.getId(), 1));

        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));
        given(productRepository.findById(anyLong())).willReturn(Optional.empty(), Optional.of(삼선짬뽕));

        // when
        ThrowableAssert.ThrowingCallable callable = () -> menuService.create(menuRequest);

        // then
        assertThatThrownBy(callable)
                .isInstanceOf(ProductNotFoundException.class);
    }

    @DisplayName("모든 메뉴를 조회한다")
    @Test
    void testList() {
        // given
        List<Menu> expectedMenus = Arrays.asList(new Menu(1L, "대표 메뉴", 16000, new MenuGroup(), Collections.emptyList()));
        given(menuRepository.findAll()).willReturn(expectedMenus);

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus).isEqualTo(MenuResponse.ofList(expectedMenus));
    }
}
