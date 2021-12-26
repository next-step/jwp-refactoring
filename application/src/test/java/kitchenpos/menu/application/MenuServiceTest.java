package kitchenpos.menu.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.exception.ProductNotFoundException;
import kitchenpos.product.domain.Product;
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
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuValidator menuValidator;
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
        menuProducts.add(new MenuProduct(볶음짜장면.getId(), 1));
        menuProducts.add(new MenuProduct(삼선짬뽕.getId(), 1));
        Menu expectedMenu = new Menu(1L, Name.of("집밥이최고"), Price.of(16000), 1L, new MenuProducts(menuProducts));

        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));
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
        List<MenuProductRequest> menuProducts = new ArrayList<>();
        MenuGroup menuGroup = new MenuGroup(1L, "식사류");
        MenuRequest menuRequest = new MenuRequest(1L, "대표메뉴", 16000L, menuGroup.getId(), menuProducts);

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

        List<MenuProductRequest> menuProductList = new ArrayList<>();
        MenuGroup menuGroup = new MenuGroup(1L, "식사류");
        MenuRequest menuRequest = new MenuRequest(1L, "대표메뉴", 16000L, menuGroup.getId(), menuProductList);
        menuProductList.add(new MenuProductRequest(볶음짜장면.getId(), 1));
        menuProductList.add(new MenuProductRequest(삼선짬뽕.getId(), 1));

        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));
        doThrow(ProductNotFoundException.class).when(menuValidator).validateHasProducts(Arrays.asList(볶음짜장면.getId(), 삼선짬뽕.getId()));

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
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1));
        menuProducts.add(new MenuProduct(2L, 1));
        List<Menu> expectedMenus = Arrays.asList(new Menu(1L, Name.of("집밥이최고"), Price.of(16000), 1L, new MenuProducts(menuProducts)));
        given(menuRepository.findAll()).willReturn(expectedMenus);

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus).isEqualTo(MenuResponse.ofList(expectedMenus));
    }
}
