package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.dto.MenuProductDTO;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    private Product chicken;
    private Product ham;
    private MenuProductDTO chicken_menuProduct;
    private MenuProductDTO ham_menuProduct;

    @BeforeEach
    public void init() {
        menuService = new MenuService(menuRepository, menuGroupRepository, productRepository);

        chicken = new Product("chicken", BigDecimal.valueOf(5000));

        chicken_menuProduct = new MenuProductDTO();
        chicken_menuProduct.setProductId(1L);
        chicken_menuProduct.setQuantity(1L);

        ham = new Product("ham", BigDecimal.valueOf(4000));

        ham_menuProduct = new MenuProductDTO();
        ham_menuProduct.setProductId(2L);
        ham_menuProduct.setQuantity(1L);
    }

    @Test
    @DisplayName("메뉴 생성 정상로직")
    void createMenuHappyCase() {
        //given
        MenuRequest menuRequest = new MenuRequest();
        menuRequest.setName("치킨과햄");
        menuRequest.setPrice(BigDecimal.valueOf(1000));
        menuRequest.setMenuProducts(Arrays.asList(chicken_menuProduct, ham_menuProduct));

        when(productRepository.findById(1L)).thenReturn(Optional.of(chicken));
        when(productRepository.findById(2L)).thenReturn(Optional.of(ham));
        when(menuGroupRepository.findById(menuRequest.getMenuGroupId())).thenReturn(
            Optional.of(new MenuGroup("한마리메뉴")));
        Menu expectedMenu = new Menu(menuRequest.getName(), menuRequest.getPrice(),
            menuRequest.getMenuGroupId());
        when(menuRepository.save(any())).thenReturn(expectedMenu);

        //when && then
        assertDoesNotThrow(() -> menuService.create(menuRequest));
    }

    @Test
    @DisplayName("음수의 가격 및 Null로 메뉴 생성시 에러 발생")
    void createWithMinusPriceThrowError() {
        //given
        MenuRequest menu_minusPrice = new MenuRequest();
        menu_minusPrice.setPrice(BigDecimal.valueOf(-1));

        MenuRequest menu_nullPrice = new MenuRequest();
        menu_nullPrice.setPrice(null);

        //when
        assertAll(
            () -> assertThatThrownBy(() -> menuService.create(menu_minusPrice))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> menuService.create(menu_nullPrice))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("어느 메뉴그룹에도 속해있지 않으면 에러발생")
    void createWithNoMenuGroupThrowError() {
        //given
        MenuRequest menu = new MenuRequest();
        menu.setPrice(BigDecimal.valueOf(1000));

        MenuProductDTO menuProductDTONotSaved = new MenuProductDTO();
        menuProductDTONotSaved.setProductId(999L);
        menu.setMenuProducts(Arrays.asList(menuProductDTONotSaved));

        //when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 속해있는 상품들의 합보다 가격이 크면 에러 발생")
    void createWithTooMuchPriceThrowError() {
        //given
        MenuRequest menu = new MenuRequest();
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuProducts(Arrays.asList(chicken_menuProduct, ham_menuProduct));

        when(productRepository.findById(1L)).thenReturn(Optional.of(chicken));
        when(productRepository.findById(2L)).thenReturn(Optional.of(ham));

        //when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

}