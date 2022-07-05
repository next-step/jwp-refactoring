package kitchenpos.menu.application;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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

//import static kitchenpos.factory.fixture.MenuFixtureFactory.createMenu;
//import static kitchenpos.factory.fixture.MenuGroupFixtureFactory.createMenuGroup;
//import static kitchenpos.factory.fixture.MenuProductFixtureFactory.createMenuProduct;
//import static kitchenpos.factory.fixture.ProductFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

    Long 양념_치킨_ID;
    Long 치킨_그룹_ID;

    private Product 콜라;
    private Product 닭;
    private MenuProduct 양념_치킨_콜라;
    private MenuProduct 양념_치킨_닭;
    private MenuGroup 치킨_그룹;
    private Menu 양념_치킨;
    private MenuProductRequest menuProductRequest;
    private MenuRequest menuRequest;

    @BeforeEach
    void setUp() {
        양념_치킨_ID = 1L;
        치킨_그룹_ID = 1L;

        콜라 = new Product(1L, "밀가루", BigDecimal.valueOf(5000L));
        닭 = new Product(2L, "닭", BigDecimal.valueOf(10000L));
        양념_치킨_콜라 = new MenuProduct(1L, 양념_치킨, 콜라, 3);
        양념_치킨_닭 = new MenuProduct(2L, 양념_치킨, 닭, 1);
        치킨_그룹 = new MenuGroup(치킨_그룹_ID, "치킨");
        양념_치킨 = new Menu(양념_치킨_ID, "양념치킨", BigDecimal.valueOf(20000L), 치킨_그룹.getId(), Arrays.asList(양념_치킨_콜라, 양념_치킨_닭));
        menuProductRequest = new MenuProductRequest(양념_치킨_콜라.getProduct().getId(), 양념_치킨_콜라.getQuantity());
        menuRequest = new MenuRequest(양념_치킨.getName(), 양념_치킨.getPrice(), 양념_치킨.getMenuGroupId(), Arrays.asList(menuProductRequest));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        MenuProductRequest menuProductRequest_2 = new MenuProductRequest(양념_치킨_닭.getProduct().getId(), 양념_치킨_닭.getQuantity());
        menuRequest = new MenuRequest(양념_치킨.getName(), 양념_치킨.getPrice(), 양념_치킨.getMenuGroupId(),
                Arrays.asList(menuProductRequest, menuProductRequest_2));

        given(menuGroupRepository.existsById(치킨_그룹.getId())).willReturn(Boolean.TRUE);
        given(productRepository.findById(양념_치킨_콜라.getProduct().getId())).willReturn(Optional.of(콜라));
        given(productRepository.findById(양념_치킨_닭.getProduct().getId())).willReturn(Optional.of(닭));
        given(menuRepository.save(any(Menu.class))).willReturn(양념_치킨);


        MenuResponse response = menuService.create(menuRequest);

        assertThat(response.getName()).isEqualTo(양념_치킨.getName());
    }

    @DisplayName("메뉴의 가격이 0원 이상이 아니라면 예외를 던진다.")
    @Test
    void createWithInvalidPrice() {
        MenuProductRequest menuProductRequest_2 = new MenuProductRequest(양념_치킨_닭.getProduct().getId(), 양념_치킨_닭.getQuantity());

        menuRequest = new MenuRequest(양념_치킨.getName(), BigDecimal.valueOf(-1L), 양념_치킨.getMenuGroupId(),
                Arrays.asList(menuProductRequest, menuProductRequest_2));

        given(menuGroupRepository.existsById(menuRequest.getMenuGroupId())).willReturn(Boolean.TRUE);
        given(productRepository.findById(menuProductRequest.getProductId())).willReturn(Optional.of(콜라));
        given(productRepository.findById(menuProductRequest_2.getProductId())).willReturn(Optional.of(닭));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_PRICE.getMessage());
    }

    @DisplayName("메뉴 그룹에 등록되지 않았다면 예외를 던진다.")
    @Test
    void createWihInvalidMeuGroupId() {
        MenuGroup menuGroup = new MenuGroup(1000L, "테스트");
        Menu menu = new Menu(1000L, null, BigDecimal.valueOf(1000L), menuGroup.getId(), null);

        menuRequest = new MenuRequest(menu.getName(), menu.getPrice(), menu.getMenuGroupId(), Arrays.asList());

        given(menuGroupRepository.existsById(menuGroup.getId())).willReturn(Boolean.FALSE);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.MENU_GROUP_NOT_FOUND.getMessage());

    }

    @DisplayName("상품이 존재하지 않는다면 예외를 던진다.")
    @Test
    void createWihInvalidProductId() {
        given(menuGroupRepository.existsById(치킨_그룹.getId())).willReturn(Boolean.TRUE);
        given(productRepository.findById(양념_치킨_콜라.getProduct().getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
    }

    @DisplayName("상품 금액의 합보다 메뉴 금액이 더 크면 예외를 던진다.")
    @Test
    void createWithInvalidSum() {
        Product 닭 = new Product(1L, "밀가루", BigDecimal.valueOf(10000L));
        Product 콜라 = new Product(2L, "닭", BigDecimal.valueOf(4000L));

        given(menuGroupRepository.existsById(양념_치킨.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(양념_치킨_콜라.getProduct().getId())).willReturn(Optional.of(콜라));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_MENU_PRICE.getMessage());
    }

    @Test
    void list() {
        Menu 양념_치킨 = new Menu(양념_치킨_ID, "양념치킨", BigDecimal.valueOf(20000L), 치킨_그룹.getId(), Arrays.asList(양념_치킨_콜라, 양념_치킨_닭));
        given(menuRepository.findAllWithMenuProducts()).willReturn(Arrays.asList(양념_치킨));

        List<MenuResponse> response = menuService.list();

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getName()).isEqualTo("양념치킨");
    }
}
