package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    private Product 참치김밥;
    private Product 치즈김밥;
    private Product 라볶이;
    private Product 돈까스;
    private Product 쫄면;

    private MenuGroup 분식;

    private MenuProduct 라볶이세트참치김밥;
    private MenuProduct 라볶이세트라볶이;
    private MenuProduct 라볶이세트돈까스;

    private MenuProduct 쫄면세트치즈김밥;
    private MenuProduct 쫄면세트쫄면;
    private MenuProduct 쫄면세트돈까스;

    private MenuProducts 라볶이세트구성;
    private MenuProducts 쫄면세트구성;

    private Menu 라볶이세트;
    private Menu 쫄면세트;

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuProductRepository menuProductRepository;
    private MenuGroupService menuGroupService;
    private MenuProductValidator menuProductValidator;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        참치김밥 = new Product(1L, "참치김밥", new Price(new BigDecimal(3000)));
        치즈김밥 = new Product(2L, "치즈김밥", new Price(new BigDecimal(2500)));
        라볶이 = new Product(3L, "라볶이", new Price(new BigDecimal(4500)));
        돈까스 = new Product(4L, "돈까스", new Price(new BigDecimal(7000)));
        쫄면 = new Product(5L, "쫄면", new Price(new BigDecimal(5000)));

        분식 = new MenuGroup(1L, "분식");

        라볶이세트참치김밥 = new MenuProduct(참치김밥, new Quantity(1));
        라볶이세트라볶이 = new MenuProduct(라볶이, new Quantity(1));
        라볶이세트돈까스 = new MenuProduct(돈까스, new Quantity(1));

        쫄면세트치즈김밥 = new MenuProduct(치즈김밥, new Quantity(1));
        쫄면세트쫄면 = new MenuProduct(쫄면, new Quantity(1));
        쫄면세트돈까스 = new MenuProduct(돈까스, new Quantity(1));

        라볶이세트구성 = new MenuProducts(Arrays.asList(라볶이세트참치김밥, 라볶이세트라볶이, 라볶이세트돈까스));
        쫄면세트구성 = new MenuProducts(Arrays.asList(쫄면세트치즈김밥, 쫄면세트쫄면, 쫄면세트돈까스));

        라볶이세트 = new Menu(1L, "라볶이세트", new Price(new BigDecimal(14000)), 분식, 라볶이세트구성);
        쫄면세트 = new Menu(2L, "쫄면세트", new Price(new BigDecimal(14000)), 분식, 쫄면세트구성);

        menuGroupService = new MenuGroupService(menuGroupRepository);
        menuProductValidator = new MenuProductValidator(menuProductRepository, productRepository);
        menuService = new MenuService(menuRepository, menuGroupService, menuProductValidator);
    }

    @DisplayName("메뉴등록 테스트")
    @Test
    void createMenuTest() {
        //given
        when(menuGroupRepository.findById(분식.getId()))
                .thenReturn(Optional.ofNullable(분식));
        when(productRepository.findById(참치김밥.getId()))
                .thenReturn(Optional.ofNullable(참치김밥));
        when(productRepository.findById(라볶이.getId()))
                .thenReturn(Optional.ofNullable(라볶이));
        when(productRepository.findById(돈까스.getId()))
                .thenReturn(Optional.ofNullable(돈까스));
        when(menuRepository.save(any(Menu.class)))
                .thenReturn(라볶이세트);

        //when
        final MenuResponse menu = menuService.create(menuToMenuRequest(라볶이세트));

        //then
        checkMenu(menu, 라볶이세트);
    }

    private MenuRequest menuToMenuRequest(Menu menu) {
        return new MenuRequest(menu.getName(),
                menu.getPrice().getValue(),
                menu.getMenuGroup().getId(),
                menu.getMenuProducts()
                        .getValue()
                        .stream()
                        .map(this::menuProductsToMenuProductRequests)
                        .collect(Collectors.toList()));
    }

    private void checkMenu(MenuResponse menu, Menu expectedMenu) {
        final List<MenuProduct> menuProducts = expectedMenu.getMenuProducts()
                        .getValue();
        assertAll(
                () -> assertThat(menu.getName())
                        .isEqualTo(expectedMenu.getName()),
                () -> assertThat(menu.getPrice())
                        .isEqualTo(expectedMenu.getPrice().getValue()),
                () -> assertThat(menu.getMenuGroupResponse().getName())
                        .isEqualTo(expectedMenu.getMenuGroup().getName()),
                () -> assertThat(menu.getMenuProductResponses()
                        .stream()
                        .map(menuProductResponse -> menuProductResponse.getProductResponse().getName())
                        .collect(Collectors.toList()))
                        .containsAll(menuProductsToNames(menuProducts)),
                () -> assertThat(menu.getMenuProductResponses()
                        .stream()
                        .map(menuProductResponse -> menuProductResponse.getQuantity())
                        .collect(Collectors.toList()))
                        .containsAll(menuProductsToQuantities(menuProducts))
        );

    }

    private MenuProductRequest menuProductsToMenuProductRequests(MenuProduct menuProduct) {
        final Product product = menuProduct.getProduct();
        final Quantity quantity = menuProduct.getQuantity();

        return new MenuProductRequest(product.getId(),
                quantity.getValue());
    }

    private List<Long> menuProductsToQuantities(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.getQuantity().getValue())
                .collect(Collectors.toList());
    }

    private Iterable<String> menuProductsToNames(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.getProduct().getName())
                .collect(Collectors.toList());
    }

    @DisplayName("메뉴목록 조회 테스트")
    @Test
    void RetrieveMenuListTest() {
        //given
        final List<Menu> 메뉴목록 = Arrays.asList(라볶이세트, 쫄면세트);
        Map<String, Menu> 메뉴이름별메뉴 = new HashMap<>();
        메뉴이름별메뉴.put(라볶이세트.getName(), 라볶이세트);
        메뉴이름별메뉴.put(쫄면세트.getName(), 쫄면세트);

        when(menuRepository.findAll())
                .thenReturn(메뉴목록);

        //when
        final List<MenuResponse> menus = menuService.list();

        //then
        for (MenuResponse menuResponse : menus) {
            final String menuName = menuResponse.getName();
            assertThat(메뉴이름별메뉴.get(menuName))
                    .isNotNull();
            checkMenu(menuResponse, 메뉴이름별메뉴.get(menuName));
        }
    }

    @DisplayName("메뉴등록 가격정보 없을 경우 오류 테스트")
    @Test
    void createMenuPriceNullExceptionTest() {
        //when
        //then
        assertThatThrownBy(() -> menuService.create(new MenuRequest(라볶이세트.getName(), null,
                라볶이세트.getMenuGroup().getId(),
                menuProductsToMenuProductRequests(라볶이세트구성))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<MenuProductRequest> menuProductsToMenuProductRequests(MenuProducts menuProducts) {
        return menuProducts.getValue()
                .stream()
                .map(this::menuProductsToMenuProductRequests)
                .collect(Collectors.toList());
    }


    @DisplayName("메뉴그룹이 존재하지 않는 경우 오류 테스트")
    @Test
    void notExistMenuGroupExceptionTest() {
        //given
        final Long notExistMenuGroupId = 라볶이세트.getMenuGroup()
                .getId();
        when(menuGroupRepository.findById(notExistMenuGroupId))
                .thenReturn(Optional.ofNullable(null));

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuToMenuRequest(라볶이세트)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품목록에 있는 상품 정보가 존재하지 않는 경우 오류 테스트")
    @Test
    void notExistProductInMenuProductListExceptionTest() {
        //given
        when(productRepository.findById(참치김밥.getId()))
                .thenReturn(Optional.ofNullable(참치김밥));
        when(productRepository.findById(라볶이.getId()))
                .thenReturn(Optional.ofNullable(라볶이));
        when(productRepository.findById(돈까스.getId()))
                .thenReturn(Optional.ofNullable(null));

        when(menuGroupRepository.findById(분식.getId()))
                .thenReturn(Optional.ofNullable(분식));

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuToMenuRequest(라볶이세트)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
