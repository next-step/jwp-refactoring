package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    MenuProductRepository menuProductRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    MenuService menuService;

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create1() {
        //given
        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(1L, 1));

        MenuRequest menuRequest = new MenuRequest("후라이드치킨", new BigDecimal("16000"), 2L);
        menuRequest.setMenuProducts(menuProductRequests);

        // TODO: 임시로 any() 로 돌려놓음.
        given(menuGroupRepository.existsById(any()))
                .willReturn(true);

        given(menuGroupRepository.getOne(any()))
                .willReturn(new MenuGroup(2L, "메뉴그롭1"));

        given(productRepository.findById(any()))
                .willReturn(Optional.of(new Product(1L, "후라이드치킨", new BigDecimal("16000"))));
        given(menuRepository.save(any()))
                .willReturn(
                        new Menu(
                                1L, "후라이드치킨", new BigDecimal("16000"),
                                new MenuGroup(2L, "메뉴그룹2")
                        )
                );

        //when
        MenuResponse createMenu = menuService.create(menuRequest);

        //then
        assertThat(createMenu.getId()).isEqualTo(1L);
        assertThat(createMenu.getName()).isEqualTo("후라이드치킨");
        assertThat(createMenu.getPrice()).isEqualTo(new BigDecimal("16000"));
        assertThat(createMenu.getMenuGroupId()).isEqualTo(2L);
    }

    @DisplayName("메뉴를 등록할 수 있다 - 메뉴의 가격은 0 원 이상이어야 한다.")
    @Test
    void create2() {
        MenuRequest newMenu = new MenuRequest("후라이드치킨", new BigDecimal("-1"), 2L);
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 수 있다 - 등록된 메뉴그룹이 선택되어 있어야 한다.")
    @Test
    void create3() {
        // given
        given(menuGroupRepository.existsById(any())).willReturn(false);

        MenuRequest newMenu = new MenuRequest("후라이드치킨", new BigDecimal("16000"), 2L);
        // when
        // then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴그룹이 없습니다.");
    }

    @DisplayName("메뉴를 등록할 수 있다 - 상품목록의 각 상품이 이미 등록되어 있어야 한다.")
    @Test
    void create4() {
        //given
        given(menuGroupRepository.existsById(any()))
                .willReturn(true);
        given(productRepository.findById(any()))
                .willReturn(Optional.empty());

        MenuRequest newMenu = new MenuRequest( "후라이드치킨", new BigDecimal("16000"), 2L);
        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(1L, 1));
        newMenu.setMenuProducts(menuProductRequests);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 수 있다 - 메뉴의 가격이 상품목록 총합 가격보다 더 크면 안됨")
    @Test
    void create5() {
        //given
        given(menuGroupRepository.existsById(any()))
                .willReturn(true);
        given(productRepository.findById(any()))
                .willReturn(Optional.of(new Product(1L, "후라이드치킨", new BigDecimal("14000"))));

        List<MenuProductRequest> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProductRequest(1L, 1));

        MenuRequest newMenu = new MenuRequest("후라이드치킨", new BigDecimal("16000"), 2L);
        newMenu.setMenuProducts(menuProducts);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 상품목록 총합 가격보다 더 큽니다.");
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void list() {
        //given
        MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹1");
        Menu menu1 = new Menu(1L, "후라이드치킨", new BigDecimal("16000"), menuGroup);
        Menu menu2 = new Menu(2L, "양념치킨", new BigDecimal("16000"), menuGroup);

        given(menuRepository.findAll())
                .willReturn(Arrays.asList(menu1, menu2));

        //when
        List<MenuResponse> menus = menuService.list();

        //then
        assertThat(menus.size()).isEqualTo(2);
        assertThat(menus.get(0).getName()).isEqualTo("후라이드치킨");
        assertThat(menus.get(1).getName()).isEqualTo("양념치킨");
    }
}
