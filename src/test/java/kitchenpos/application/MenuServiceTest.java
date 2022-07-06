package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.Quantity;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    private List<MenuProductRequest> menuProductRequests1;

    private Product product1;
    private Product product2;
    //private Menu menu;

    @BeforeEach
    void setUp() {
        menuProductRequests1 = Arrays.asList(
                new MenuProductRequest(1L,2),
                new MenuProductRequest(2L,1)
        );

        product1 = new Product(1L,"상품1", Price.from(1000));
        product2 = new Product(2L,"상품2", Price.from(1000));
    }


    @Test
    @DisplayName("메뉴 그룹이 없으면 메뉴를 등록할 수 없다.")
    void isNoneMenuGroup() {
        //given
        MenuRequest menuRequest = new MenuRequest("메뉴", BigDecimal.valueOf(1), 1L, menuProductRequests1);
        given(menuGroupRepository.getOne(menuRequest.getMenuGroupId())).willReturn(null);

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest));

    }


    @DisplayName("메뉴의 가격이 0 초과 이어야 이어야만 한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void menuPriceMinZero(int price) {
        //given
        MenuRequest menuRequest = new MenuRequest("메뉴", BigDecimal.valueOf(price), 1L, menuProductRequests1);
        given(menuGroupRepository.getOne(menuRequest.getMenuGroupId())).willReturn(MenuGroup.from("메뉴 그룹"));
        given(productRepository.getOne(1L)).willReturn(new Product("상품1", Price.from(100)));
        given(productRepository.getOne(2L)).willReturn(new Product("상품2", Price.from(100)));

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest));

    }

    @Test
    @DisplayName("메뉴의 가격은 메뉴 상품들의 합계보다 작아야 한다.")
    void productTotalIsBigAsMenuPriceIsBigAs() {
        //given
        MenuRequest menuRequest = new MenuRequest("메뉴", BigDecimal.valueOf(5000), 1L, menuProductRequests1);
        given(menuGroupRepository.getOne(menuRequest.getMenuGroupId())).willReturn(MenuGroup.from("메뉴 그룹"));
        given(productRepository.getOne(1L)).willReturn(new Product("상품1", Price.from(1000)));
        given(productRepository.getOne(2L)).willReturn(new Product("상품2", Price.from(1000)));


        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest));
    }

    @Test
    @DisplayName("메뉴가 등록 된다.")
    void createMenu() {
        //given
        MenuProduct menuProduct1 = new MenuProduct(1L,null, product1, Quantity.from(2));
        MenuProduct menuProduct2 = new MenuProduct(2L,null, product2, Quantity.from(1));
        Menu menu = new Menu(1L,"메뉴1", Price.from(2000), MenuGroup.from("메뉴 그룹"),
                MenuProducts.from(Arrays.asList(menuProduct1, menuProduct2)));
        MenuRequest menuRequest = new MenuRequest("메뉴", menu.getPrice().value(), 1L, menuProductRequests1);

        given(menuGroupRepository.getOne(menuRequest.getMenuGroupId())).willReturn(menu.getMenuGroup());
        given(productRepository.getOne(1L)).willReturn(product1);
        given(productRepository.getOne(2L)).willReturn(product2);
        given(menuRepository.save(any())).willReturn(menu);


        //when
        final MenuResponse saveMenu = menuService.create(menuRequest);

        //then
        assertAll("메뉴가 등록이 됨",
                () -> assertThat(saveMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(saveMenu.getMenuProducts()).hasSize(2)
        );
    }

    @Test
    @DisplayName("메뉴 목록을 조회")
    void listMenu() {
        //given
        MenuProduct menuProduct1 = new MenuProduct(1L, null, product1, Quantity.from(2));
        MenuProduct menuProduct2 = new MenuProduct(2L, null, product2, Quantity.from(1));
        MenuProduct menuProduct3 = new MenuProduct(3L, null, product1, Quantity.from(3));
        Menu menu1 = new Menu(1L,"메뉴1", Price.from(2000), MenuGroup.from("메뉴 그룹"),
                MenuProducts.from(Arrays.asList(menuProduct1, menuProduct2)));
        Menu menu2 = new Menu(2L,"메뉴2", Price.from(2000), MenuGroup.from("메뉴 그룹"),
                MenuProducts.from(Collections.singletonList(menuProduct3)));

        given(menuRepository.findAll()).willReturn(Arrays.asList(menu1, menu2));


        //when
        final List<MenuResponse> menus = menuService.list();

        //then
        assertThat(menus).hasSize(2);
        assertThat(menus).extracting("name").contains("메뉴1", "메뉴2");
    }


    @Test
    @DisplayName("메뉴 단건 조회")
    void menuSearch() {
        //given
        MenuProduct menuProduct1 = new MenuProduct(1L, null, product1, Quantity.from(2));
        MenuProduct menuProduct2 = new MenuProduct(2L, null, product2, Quantity.from(1));

        Menu menu = new Menu(1L,"메뉴1", Price.from(2000), MenuGroup.from("메뉴 그룹"),
                MenuProducts.from(Arrays.asList(menuProduct1, menuProduct2)));

        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));


        //when
        final MenuResponse menuResponse = menuService.findByMenuId(1L);


        //then
        assertThat(menuResponse).isNotNull();
        assertThat(menuResponse.getMenuProducts()).hasSize(2);
    }

    @Test
    @DisplayName("메뉴가 존재하지 않는다.")
    void noMenuSearch() {
        given(menuRepository.findById(1L)).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> menuService.findByMenuId(1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
