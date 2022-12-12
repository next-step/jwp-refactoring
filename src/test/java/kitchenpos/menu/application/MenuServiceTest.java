package kitchenpos.menu.application;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.generator.BuilderArbitraryGenerator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.persistence.MenuGroupRepository;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.persistence.ProductRepository;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @InjectMocks
    private MenuService menuService;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;

    @DisplayName("메뉴가 속한 메뉴그룹이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsMeneGroup() {
        MenuRequest menuRequest = new MenuRequest("메뉴", BigDecimal.valueOf(0), 15l, Collections.EMPTY_LIST);
        doReturn(Optional.empty()).when(menuGroupRepository).findById(anyLong());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴룰 추가하면 메뉴정보를 반환")
    @Test
    public void returnMenu() {
        MenuRequest menuRequest = new MenuRequest("메뉴", BigDecimal.valueOf(0), 15l, Collections.EMPTY_LIST);
        Menu menu = Menu.builder().id(15l)
                .menuProducts(Arrays.asList(MenuProduct.builder().menu(Menu.builder().build()).build()))
                .build();
        doReturn(Optional.ofNullable(MenuGroup.builder().build())).when(menuGroupRepository).findById(anyLong());
        doReturn(Arrays.asList(Product.builder()
                .build())).when(productRepository).findAllById(anyList());
        doReturn(menu).when(menuRepository).save(any(Menu.class));

        MenuResponse returnedMenu = menuService.create(menuRequest);

        assertThat(returnedMenu.getId()).isEqualTo(15l);
    }

    @DisplayName("메뉴가격이 메뉴구성상품들의 총 가격 높은경우 예외발생")
    @Test
    public void throwsExceptionWhenMenuPriceGreater() {
        MenuRequest menuRequest = new MenuRequest("메뉴", BigDecimal.valueOf(15000), 15l, Collections.EMPTY_LIST);
        doReturn(Optional.ofNullable(MenuGroup.builder().build())).when(menuGroupRepository).findById(anyLong());
        doReturn(Arrays.asList(Product.builder()
                .build())).when(productRepository).findAllById(anyList());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("메뉴목록을 조회하는경우 메뉴목록을 반환")
    @Test
    public void returnMenus() {
        List<Menu> menus = getMenus(Menu.builder()
                .id(Arbitraries.longs().between(1, 1000l).sample())
                .menuProducts(getMenuProducts(MenuProduct.builder().menu(Menu.builder().build()).build(), 3))
                .menuGroup(MenuGroup.builder().build())
                .build(), 5);
        doReturn(menus)
                .when(menuRepository)
                .findAll();

        List<MenuResponse> returnedMenus = menuService.list();

        assertThat(returnedMenus.stream().map(MenuResponse::getId).collect(Collectors.toList()))
                .containsAll(menus.stream().map(menu -> menu.getId()).collect(Collectors.toList()));
    }

    private List<Menu> getMenus(Menu menu, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(value -> Menu.builder()
                        .id(menu.getId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .menuProducts(menu.getMenuProducts())
                        .menuGroup(menu.getMenuGroup())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MenuProduct> getMenuProducts(MenuProduct menuProduct, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(value -> MenuProduct.builder()
                        .seq(menuProduct.getSeq())
                        .product(menuProduct.getProduct())
                        .menu(menuProduct.getMenu())
                        .quantity(menuProduct.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

}
