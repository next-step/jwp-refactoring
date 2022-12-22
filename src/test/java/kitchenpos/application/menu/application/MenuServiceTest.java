package kitchenpos.application.menu.application;

import java.util.ArrayList;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuGroupService menuGroupService;

    @Mock
    private ProductService productService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    private Menu 기본메뉴;
    private MenuGroup 기본메뉴그룹;
    private MenuProduct 기본메뉴_아메리카노;
    private MenuProduct 기본메뉴_바닐라라떼;
    private Product 아메리카노;
    private Product 바닐라라떼;
    private MenuRequest 메뉴기본요청;
    private MenuProductRequest 아메리카노요청;

    @BeforeEach
    void setUp() {
        아메리카노 = new Product("아메리카노", BigDecimal.valueOf(5000));
        바닐라라떼 = new Product("바닐라라떼", BigDecimal.valueOf(5000));
        기본메뉴그룹 = new MenuGroup("기본메뉴그룹");
        기본메뉴_아메리카노 = new MenuProduct(아메리카노, 1);
        기본메뉴_바닐라라떼 = new MenuProduct(바닐라라떼, 1);
        기본메뉴 = new Menu("기본메뉴", BigDecimal.valueOf(5000), 기본메뉴그룹,
            Arrays.asList(기본메뉴_아메리카노, 기본메뉴_바닐라라떼));
        아메리카노요청 = new MenuProductRequest(아메리카노.getId(), 2L);
        메뉴기본요청 = new MenuRequest("메뉴 기본", BigDecimal.valueOf(5_000), 기본메뉴그룹.getId(),
            Arrays.asList(아메리카노요청));

    }


    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        when(menuGroupService.findById(any())).thenReturn(기본메뉴그룹);
        when(productService.findMenuProducts(any())).thenReturn(Arrays.asList(기본메뉴_아메리카노));
        when(menuRepository.save(any())).thenReturn(기본메뉴);

        // when
        MenuResponse 메뉴_기본_등록 = menuService.create(메뉴기본요청);

        // then
        assertThat(메뉴_기본_등록.getName()).isEqualTo(기본메뉴.getName());
    }

    @Test
    @DisplayName("메뉴의 가격이 0원 미만이면 오류 발생한다.")
    void createUnderZeroPriceMenuException() {
        // given
        MenuRequest 가격zero미만 = new MenuRequest("가격zero미만", BigDecimal.valueOf(-7000L),
            기본메뉴그룹.getId(), null);

        // then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(가격zero미만));
    }

    @Test
    @DisplayName("메뉴 그룹이 등록되어 있지 않으면 오류 발생한다.")
    void notExistMenuGroupException() {
        // given
        when(menuGroupService.findById(기본메뉴그룹.getId())).thenThrow(IllegalArgumentException.class);

        // when && then
        assertThatThrownBy(() -> menuService.create(메뉴기본요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void findAllMenu() {
        // given
        given(menuRepository.findAll()).willReturn(Arrays.asList(기본메뉴));

        // when
        List<MenuResponse> menuList = menuService.list();

        // then
        assertAll(
            () -> assertThat(menuList).hasSize(1),
            () -> assertThat(menuList.get(0).getId()).isEqualTo(기본메뉴.getId()),
            () -> assertThat(menuList.get(0).getName()).isEqualTo(기본메뉴.getName())
        );
    }

}
