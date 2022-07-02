package kitchenpos.menu.application;

import kitchenpos.exception.IllegalPriceException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.NoSuchMenuGroupException;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.NoSuchProductException;
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

import static kitchenpos.utils.fixture.MenuFixtureFactory.createMenu;
import static kitchenpos.utils.fixture.MenuGroupFixtureFactory.createMenuGroup;
import static kitchenpos.utils.fixture.MenuProductFixtureFactory.createMenuProduct;
import static kitchenpos.utils.fixture.ProductFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupService menuGroupService;
    @Mock
    private MenuProductRepository menuProductDao;
    @Mock
    private ProductService productService;
    @InjectMocks
    private MenuService menuService;

    private Menu 메뉴_김치찌개세트;
    private MenuGroup 메뉴그룹_한식;
    private Product 김치찌개;
    private MenuProduct 김치찌개세트_김치찌개;

    @BeforeEach
    void setUp() {
        메뉴그룹_한식 = createMenuGroup(1L, "한식메뉴");
        김치찌개 = createProduct(1L, "김치찌개", 8000);
        김치찌개세트_김치찌개 = createMenuProduct(김치찌개, 2);
        메뉴_김치찌개세트 = createMenu("김치찌개세트", 15000, 메뉴그룹_한식,
                Arrays.asList(김치찌개세트_김치찌개));
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void 메뉴_등록(){
        //given
        given(menuGroupService.findMenuGroupById(anyLong())).willReturn(메뉴그룹_한식);
        given(productService.findProductById(anyLong())).willReturn(김치찌개);
        given(menuRepository.save(any(Menu.class))).willReturn(메뉴_김치찌개세트);

        //when
        MenuRequest menuRequest = MenuRequest.of(
                메뉴_김치찌개세트.getName(), 메뉴_김치찌개세트.getPrice(), 메뉴_김치찌개세트.getMenuGroup().getId(),
                Arrays.asList(
                        MenuProductRequest.of(김치찌개세트_김치찌개.getProduct().getId(), 김치찌개세트_김치찌개.getQuantity())
                )
        );
        MenuResponse menuResponse = menuService.create(menuRequest);

        //then
        assertThat(menuResponse).isEqualTo(MenuResponse.from(메뉴_김치찌개세트));
    }

    @DisplayName("메뉴의 가격은 0 이상이어야 한다")
    @Test
    void 메뉴_등록_가격_검증_0이상(){
        MenuRequest invalidMenuRequest = MenuRequest.of(
                메뉴_김치찌개세트.getName(), -15000, 메뉴_김치찌개세트.getMenuGroup().getId(),
                Arrays.asList(
                        MenuProductRequest.of(김치찌개세트_김치찌개.getProduct().getId(), 김치찌개세트_김치찌개.getQuantity())
                )
        );

        //then
        assertThrows(IllegalPriceException.class, () -> menuService.create(invalidMenuRequest));
    }

    @DisplayName("메뉴의 가격은, 메뉴상품의 정가의 합보다 클 수 없다")
    @Test
    void 메뉴_등록_가격_검증_정가이하(){
        //given
        given(menuGroupService.findMenuGroupById(anyLong())).willReturn(메뉴그룹_한식);
        given(productService.findProductById(anyLong())).willReturn(김치찌개);

        MenuRequest invalidMenuRequest = MenuRequest.of(
                메뉴_김치찌개세트.getName(), 20000, 메뉴_김치찌개세트.getMenuGroup().getId(),
                Arrays.asList(
                        MenuProductRequest.of(김치찌개세트_김치찌개.getProduct().getId(), 김치찌개세트_김치찌개.getQuantity())
                )
        );

        //then
        assertThrows(IllegalPriceException.class, () -> menuService.create(invalidMenuRequest));
    }

    @DisplayName("등록하려는 메뉴그룹이 존재해야 한다")
    @Test
    void 메뉴_등록_메뉴그룹_검증(){
        //given
        given(menuGroupService.findMenuGroupById(anyLong())).willThrow(NoSuchMenuGroupException.class);

        //when
        MenuRequest invalidMenuRequest = MenuRequest.of(
                메뉴_김치찌개세트.getName(), 15000, 메뉴_김치찌개세트.getMenuGroup().getId(),
                Arrays.asList(
                        MenuProductRequest.of(김치찌개세트_김치찌개.getProduct().getId(), 김치찌개세트_김치찌개.getQuantity())
                )
        );

        //then
        assertThrows(NoSuchMenuGroupException.class, () -> menuService.create(invalidMenuRequest));
    }

    @DisplayName("등록하려는 메뉴상품의 상품이 존재해야 한다")
    @Test
    void 메뉴_등록_메뉴상품_검증(){
        //given
        given(menuGroupService.findMenuGroupById(anyLong())).willReturn(메뉴그룹_한식);
        given(productService.findProductById(anyLong())).willThrow(NoSuchProductException.class);

        //when
        MenuRequest invalidMenuRequest = MenuRequest.of(
                메뉴_김치찌개세트.getName(), 15000, 메뉴_김치찌개세트.getMenuGroup().getId(),
                Arrays.asList(
                        MenuProductRequest.of(김치찌개세트_김치찌개.getProduct().getId(), 김치찌개세트_김치찌개.getQuantity())
                )
        );

        //then
        assertThrows(NoSuchProductException.class, () -> menuService.create(invalidMenuRequest));
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다")
    @Test
    void 메뉴_목록_조회(){
        //given
        given(menuRepository.findAll()).willReturn(Arrays.asList(메뉴_김치찌개세트));

        //when
        List<MenuResponse> list = menuService.list();

        //then
        assertThat(list).containsExactly(MenuResponse.from(메뉴_김치찌개세트));
    }
}