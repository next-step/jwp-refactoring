package kitchenpos.menu.application.menu;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menu.MenuGroup;
import kitchenpos.menu.domain.menu.MenuGroupRepository;
import kitchenpos.menu.domain.menu.MenuProduct;
import kitchenpos.menu.domain.menu.MenuRepository;
import kitchenpos.menu.domain.product.Product;
import kitchenpos.menu.domain.product.ProductRepository;
import kitchenpos.menu.dto.menu.MenuProductRequest;
import kitchenpos.menu.dto.menu.MenuRequest;
import kitchenpos.menu.dto.menu.MenuResponse;

@DisplayName("애플리케이션 테스트 보호 - 메뉴 서비스")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    private Product 후라이드치킨;
    private Product 양념치킨;
    private MenuGroup 치킨세트;
    private Menu 후라이드한마리_양념치킨한마리;
    private MenuProduct 후라이드치킨한마리;
    private MenuProduct 양념치킨한마리;
    private List<Long> productIds;
    private MenuRequest 후라이드한마리_양념치킨한마리_요청;

    @BeforeEach
    public void setup() {
        후라이드치킨 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));

        양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(16000));

        치킨세트 = new MenuGroup("후라이드앙념치킨");

        후라이드한마리_양념치킨한마리 = new Menu("후라이드+양념", BigDecimal.valueOf(32000), 치킨세트);

        후라이드치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 후라이드치킨, 1L);
        양념치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 양념치킨, 1L);

        후라이드한마리_양념치킨한마리.addMenuProducts(Arrays.asList(후라이드치킨한마리, 양념치킨한마리));

        productIds = 후라이드한마리_양념치킨한마리.getMenuProducts().stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());

        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(후라이드치킨한마리.getProductId(), 후라이드치킨한마리.getQuantity()));
        menuProductRequests.add(new MenuProductRequest(양념치킨한마리.getProductId(), 양념치킨한마리.getQuantity()));

        후라이드한마리_양념치킨한마리_요청 = new MenuRequest(후라이드한마리_양념치킨한마리.getName(),
            후라이드한마리_양념치킨한마리.getPrice(), 치킨세트.getId(), menuProductRequests);
    }

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 생성")
    @Test
    void create() {

        given(menuGroupRepository.findById(치킨세트.getId())).willReturn(Optional.of(치킨세트));
        given(productRepository.findAllById(productIds)).willReturn(Arrays.asList(후라이드치킨, 양념치킨));
        given(menuRepository.save(후라이드한마리_양념치킨한마리)).willReturn(후라이드한마리_양념치킨한마리);

        MenuResponse 후라이드한마리_양념치킨한마리_응답 = menuService.create(후라이드한마리_양념치킨한마리_요청);

        assertThat(후라이드한마리_양념치킨한마리_응답).isNotNull();
        assertThat(후라이드한마리_양념치킨한마리_응답.getName()).isEqualTo(후라이드한마리_양념치킨한마리.getName());
        assertThat(후라이드한마리_양념치킨한마리_응답.getPrice()).isEqualTo(후라이드한마리_양념치킨한마리.getPrice());
        assertThat(후라이드한마리_양념치킨한마리_응답.getMenuGroup().getName())
            .isEqualTo(후라이드한마리_양념치킨한마리.getMenuGroup().getName());
        assertThat(후라이드한마리_양념치킨한마리_응답.getMenuProducts())
            .hasSize(후라이드한마리_양념치킨한마리.getMenuProducts().size());

    }

    @DisplayName("메뉴 생성 예외: 메뉴 가격이 없음")
    @Test
    void createThrowExceptionWhenNoMenuPrice() {
        후라이드한마리_양념치킨한마리_요청.setPrice(null);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드한마리_양념치킨한마리_요청));
    }

    @DisplayName("메뉴 생성 예외: 메뉴 가격이 0보다 작음")
    @Test
    void createThrowExceptionWhenMenuPriceLessThanZero() {
        후라이드한마리_양념치킨한마리_요청.setPrice(BigDecimal.valueOf(-1));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드한마리_양념치킨한마리_요청));
    }

    @DisplayName("메뉴 생성 예외: 메뉴 그룹이 없음")
    @Test
    void createThrowExceptionWhenNoMenuGroup() {
        given(menuGroupRepository.findById(치킨세트.getId())).willReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드한마리_양념치킨한마리_요청));
    }

    @DisplayName("메뉴 생성 예외: 메뉴에 속한 메뉴상품 목록 중 데이터베이스에 없는 상품이 있음")
    @Test
    void createThrowExceptionWhenNoMenuProducts() {
        given(menuGroupRepository.findById(치킨세트.getId())).willReturn(Optional.of(치킨세트));
        given(productRepository.findAllById(productIds)).willReturn(Collections.singletonList(후라이드치킨));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드한마리_양념치킨한마리_요청))
        .withMessageContaining("상품 정보가 없습니다");
    }

    @DisplayName("메뉴 생성 예외: 조회한 상품의 가격과 메뉴 상품의 수량을 곱한 금액의 합이 메뉴의 가격보다 작음")
    @Test
    void createThrowExceptionWhenMenuPriceNotEqualsSumOfProductPriceAndMenuProductQuantity() {
        given(menuGroupRepository.findById(치킨세트.getId())).willReturn(Optional.of(치킨세트));
        given(productRepository.findAllById(productIds)).willReturn(Arrays.asList(후라이드치킨, 양념치킨));
        후라이드한마리_양념치킨한마리_요청.setPrice(BigDecimal.valueOf(99999));
        Menu 가격이다른_메뉴 = new Menu(후라이드한마리_양념치킨한마리_요청.getName(),
            후라이드한마리_양념치킨한마리_요청.getPrice(), 치킨세트);
        given(menuRepository.save(가격이다른_메뉴)).willReturn(가격이다른_메뉴);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드한마리_양념치킨한마리_요청))
                .withMessage("메뉴 상품의 가격(상품 가격 * 수량)의 합이 메뉴의 가격보다 작습니다.");
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void list() {
        given(menuRepository.findAll()).willReturn(Collections.singletonList(후라이드한마리_양념치킨한마리));

        List<MenuResponse> menuResponses = menuService.findAll();

        assertThat(menuResponses).isNotEmpty();
        assertThat(menuResponses.get(0).getName()).isEqualTo(후라이드한마리_양념치킨한마리.getName());
        assertThat(menuResponses.get(0).getPrice()).isEqualTo(후라이드한마리_양념치킨한마리.getPrice());
        assertThat(menuResponses.get(0).getMenuGroup().getName())
            .isEqualTo(후라이드한마리_양념치킨한마리.getMenuGroup().getName());
        assertThat(menuResponses.get(0).getMenuGroup().getName())
            .isEqualTo(후라이드한마리_양념치킨한마리.getMenuGroup().getName());
        assertThat(menuResponses.get(0).getMenuProducts())
            .hasSize(후라이드한마리_양념치킨한마리.getMenuProducts().size());
    }

}
