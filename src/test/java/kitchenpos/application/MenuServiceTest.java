package kitchenpos.application;

import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.ui.dto.menu.MenuProductRequest;
import kitchenpos.ui.dto.menu.MenuRequest;
import kitchenpos.ui.dto.menu.MenuResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("메뉴 관리")
class MenuServiceTest extends DataBaseCleanSupport {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup 착한세트;
    private MenuProductRequest 후라이드치킨_세트메뉴;
    private MenuProductRequest 양념치킨_세트메뉴;
    private MenuProductRequest 족발_세트메뉴;
    private MenuProductRequest 존재하지않는_세트메뉴;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = MenuGroup.of("착한세트");
        착한세트 = menuGroupRepository.save(menuGroup);

        Product 후라이드치킨 = Product.of("후라이드치킨", BigDecimal.valueOf(10000));
        후라이드치킨_세트메뉴 = MenuProductRequest.of(productRepository.save(후라이드치킨).getId(), 3);
        Product 양념치킨 = Product.of("양념치킨", BigDecimal.valueOf(11000));
        양념치킨_세트메뉴 = MenuProductRequest.of(productRepository.save(양념치킨).getId(), 4);
        Product 족발 = Product.of("족발", BigDecimal.valueOf(15000));
        족발_세트메뉴 = MenuProductRequest.of(productRepository.save(족발).getId(), 5);
        존재하지않는_세트메뉴 = MenuProductRequest.of(0L, 1);
    }

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        //given
        MenuRequest menuRequest = MenuRequest.of(
                "10만원의 행복 파티 세트 (12인)",
                BigDecimal.valueOf(100000),
                착한세트.getId(),
                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));

        //when
        MenuResponse actual = menuService.create(menuRequest);

        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getMenuGroupId()).isEqualTo(착한세트.getId());
        assertThat(actual.getPrice().longValue()).isEqualTo(100000L);
        assertThat(actual.getName()).isEqualTo("10만원의 행복 파티 세트 (12인)");
        assertThat(actual.getMenuProducts().size()).isEqualTo(3);
    }

    @DisplayName("메뉴의 이름을 지정해야한다.")
    @Test
    void createMenuExceptionIfNameIsNull() {
        //TODO: 추가 기능 개발
    }

    @DisplayName("메뉴의 총 가격은 0원 이상이어야한다.")
    @Test
    void createMenuExceptionIfPriceIsNull() {
        //given
        MenuRequest menuRequest = MenuRequest.of(
                "10만원의 행복 파티 세트 (12인)",
                BigDecimal.valueOf(-10),
                착한세트.getId(),
                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));

        //when
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("메뉴는 속할 메뉴 그룹을 지정해야한다.")
    @Test
    void createMenuExceptionIfMenuGroupIsNull() {
        //given
        MenuRequest menuRequest = MenuRequest.of(
                "10만원의 행복 파티 세트 (12인)",
                BigDecimal.valueOf(100000),
                0L,
                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));

        //when
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("메뉴는 존재하는 상품으로 구성해야한다.")
    @Test
    void createMenuExceptionIfMenuProductIsNotExist() {
        //given
        MenuRequest menuRequest = MenuRequest.of(
                "10만원의 행복 파티 세트 (12인)",
                BigDecimal.valueOf(100000),
                착한세트.getId(),
                Lists.list(존재하지않는_세트메뉴));

        //when
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("메뉴의 메뉴상품은 갯수를 지정해야한다.")
    @Test
    void createMenuExceptionIfMenuProductQuantityIsNull() {
        //TODO: 추가 기능 개발
    }

    @DisplayName("메뉴의 총 가격이 기존 상품들의 총합 가격보다 비쌀 수 없다.")
    @Test
    void createMenuExceptionIfMenuPriceHigherThanProductTotalPrice() {
        //given
        MenuRequest menuRequest = MenuRequest.of(
                "10만원의 행복 파티 세트 (12인)",
                BigDecimal.valueOf(99999999),
                착한세트.getId(),
                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));

        //when
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("메뉴 그룹을 모두 조회한다.")
    @Test
    void list() {
        //when
        List<MenuResponse> actual = menuService.list();

        //then
        assertThat(actual.size()).isNotZero();
    }
}
