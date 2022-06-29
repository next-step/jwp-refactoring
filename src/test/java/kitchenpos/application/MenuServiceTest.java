package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.dto.MenuProductDTO;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private Product chicken;
    private Product ham;
    private MenuProductDTO chicken_menuProduct;
    private MenuProductDTO ham_menuProduct;

    @BeforeEach
    public void init() {
        menuService = new MenuService(menuRepository, menuGroupRepository, eventPublisher);

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
        MenuGroup menuGroup = new MenuGroup("testGroup");
        doNothing().when(eventPublisher).publishEvent(any());
        Menu expectedMenu = new Menu(menuRequest.getName(), menuRequest.getPrice(),
            menuGroup);
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(menuGroup));
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

        doNothing().when(eventPublisher)
            .publishEvent(any());

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
        MenuRequest menuRequest = new MenuRequest();
        menuRequest.setPrice(BigDecimal.valueOf(1000));

        MenuProductDTO menuProductDTONotSaved = new MenuProductDTO();
        menuProductDTONotSaved.setProductId(999L);
        menuProductDTONotSaved.setQuantity(2L);

        menuRequest.setMenuProducts(Arrays.asList(menuProductDTONotSaved));

        doThrow(IllegalArgumentException.class).when(eventPublisher)
            .publishEvent(any());
        //when & then
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 속해있는 상품들의 합보다 가격이 크면 에러 발생")
    void createWithTooMuchPriceThrowError() {
        //given
        MenuRequest menuRequest = new MenuRequest();
        menuRequest.setPrice(BigDecimal.valueOf(10000));
        menuRequest.setMenuProducts(Arrays.asList(chicken_menuProduct, ham_menuProduct));

        doThrow(IllegalArgumentException.class).when(eventPublisher)
            .publishEvent(any());

        //when & then
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

}