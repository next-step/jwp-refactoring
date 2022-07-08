package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static kitchenpos.testfixture.MenuTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    MenuRepository menuRepository;
    @Mock
    MenuValidator menuValidator;
    @InjectMocks
    MenuService menuService;

    private Product 싸이버거, 콜라;
    private MenuProduct 싱글세트_싸이버거, 더블세트_싸이버거, 세트_콜라;
    private MenuGroup 맘스세트메뉴;
    private List<MenuProduct> 싱글세트상품, 더블세트상품;

    @BeforeEach
    void setUp() {
        싸이버거 = createProduct("싸이버거", BigDecimal.valueOf(3_500));
        콜라 = createProduct("콜라", BigDecimal.valueOf(1_500));
        싱글세트_싸이버거 = createMenuProduct(싸이버거.getId(), 1);
        더블세트_싸이버거 = createMenuProduct(싸이버거.getId(), 2);
        세트_콜라 = createMenuProduct(콜라.getId(), 1);
        싱글세트상품 = Arrays.asList(싱글세트_싸이버거, 세트_콜라);
        더블세트상품 = Arrays.asList(더블세트_싸이버거, 세트_콜라);
        맘스세트메뉴 = createMenuGroup("맘스세트메뉴");
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create_success() {
        // given
        List<MenuProductRequest> menuProductsRequests = Arrays.asList(
                createMenuProductRequest(싸이버거.getId(), 1),
                createMenuProductRequest(콜라.getId(), 1)
        );
        MenuRequest 싱글세트 = createMenuRequest(맘스세트메뉴.getId(), "싱글세트", BigDecimal.valueOf(5_500), menuProductsRequests);
        given(menuRepository.save(any(Menu.class))).willReturn(createMenu(싱글세트.getMenuGroupId(), 싱글세트.getName(), 싱글세트.getPrice(), 싱글세트상품));

        // when
        MenuResponse saved = menuService.create(싱글세트);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo(싱글세트.getName()),
                () -> assertThat(saved.getPrice()).isEqualTo(싱글세트.getPrice())
        );
    }

    @DisplayName("메뉴 등록에 실패한다. (메뉴의 가격이 0 원 미만인 경우")
    @Test
    void create_fail() {
        // given
        List<MenuProductRequest> menuProductsRequests = Arrays.asList(
                createMenuProductRequest(싸이버거.getId(), 1),
                createMenuProductRequest(콜라.getId(), 1)
        );
        MenuRequest 싱글세트 = createMenuRequest(맘스세트메뉴.getId(), "싱글세트", BigDecimal.valueOf(-1), menuProductsRequests);

        // then
        assertThatThrownBy(() -> {
            menuService.create(싱글세트);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록에 실패한다. (메뉴가 속한 메뉴 그룹이 존재하지 않는 경우)")
    @Test
    void create_fail_invalidMenuGroup() {
        // given
        List<MenuProductRequest> menuProductsRequests = Arrays.asList(
                createMenuProductRequest(싸이버거.getId(), 1),
                createMenuProductRequest(콜라.getId(), 1)
        );
        MenuRequest 싱글세트 = createMenuRequest(맘스세트메뉴.getId(), "싱글세트", BigDecimal.valueOf(5_500), menuProductsRequests);
        willThrow(new NoSuchElementException()).given(menuValidator).validate(싱글세트);

        // then
        assertThatThrownBy(() -> {
            menuService.create(싱글세트);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("메뉴 등록에 실패한다. (메뉴 상품 가격의 총 합이 메뉴 가격보다 큰 경우")
    @Test
    void create_fail_priceOver() {
        // given
        List<MenuProductRequest> menuProductsRequests = Collections.singletonList(
                createMenuProductRequest(싸이버거.getId(), 1)
        );
        MenuRequest 싱글세트 = createMenuRequest(맘스세트메뉴.getId(), "싱글세트", BigDecimal.valueOf(2_500), menuProductsRequests);
        willThrow(new IllegalArgumentException()).given(menuValidator).validate(싱글세트);

        // then
        assertThatThrownBy(() -> {
            menuService.create(싱글세트);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<MenuProductRequest> menuProductsRequests1 = Arrays.asList(
                createMenuProductRequest(싸이버거.getId(), 1),
                createMenuProductRequest(콜라.getId(), 1)
        );
        List<MenuProductRequest> menuProductsRequests2 = Arrays.asList(
                createMenuProductRequest(싸이버거.getId(), 2),
                createMenuProductRequest(콜라.getId(), 1)
        );
        MenuRequest 싱글세트 = createMenuRequest(맘스세트메뉴.getId(), "싱글세트", BigDecimal.valueOf(5_500), menuProductsRequests1);
        MenuRequest 더블세트 = createMenuRequest(맘스세트메뉴.getId(), "더블세트", BigDecimal.valueOf(9_500), menuProductsRequests2);
        given(menuRepository.findAll()).willReturn(
                Arrays.asList(
                        createMenu(싱글세트.getMenuGroupId(), 싱글세트.getName(), 싱글세트.getPrice(), 싱글세트상품),
                        createMenu(더블세트.getMenuGroupId(), 더블세트.getName(), 더블세트.getPrice(), 더블세트상품)
                )
        );

        // when
        List<MenuResponse> menuList = menuService.list();

        // then
        assertThat(menuList).hasSize(2);
    }
}
