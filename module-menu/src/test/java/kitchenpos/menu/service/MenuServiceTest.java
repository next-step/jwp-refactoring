package kitchenpos.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.entity.Menu;
import kitchenpos.menu.domain.entity.MenuProduct;
import kitchenpos.menu.domain.value.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.entity.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.product.domain.entity.Product;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    MenuService menuService;

    MenuGroup 메뉴그룹_한마리메뉴;
    Product 프로덕트_후라이드치킨;
    MenuProduct 메뉴프로덕트_후라이드치킨_후라이드치킨;
    Menu 메뉴_후라이드;

    MenuGroupRequest 메뉴그룹_한마리메뉴_리퀘스트;
    ProductRequest 프로덕트_후라이드치킨_리퀘스트;
    MenuProductRequest 메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트;
    MenuRequest 메뉴_후라이드_리퀘스트;

    @BeforeEach
    void setUp() {
        메뉴그룹_한마리메뉴 = new MenuGroup(1L, "한마리메뉴");
        프로덕트_후라이드치킨 = new Product(1L, "후라이드치킨", BigDecimal.valueOf(18000));
        메뉴프로덕트_후라이드치킨_후라이드치킨 = new MenuProduct(1L, 프로덕트_후라이드치킨.getId(), 1);
        메뉴_후라이드 = Menu.of("후라이드", Price.of(BigDecimal.valueOf(18000)), 메뉴그룹_한마리메뉴.getId());

        메뉴그룹_한마리메뉴_리퀘스트 = new MenuGroupRequest(2L, "한마리메뉴");
        프로덕트_후라이드치킨_리퀘스트 = new ProductRequest(1L, "후라이드", BigDecimal.valueOf(16000));
        메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트 = new MenuProductRequest(프로덕트_후라이드치킨_리퀘스트.getId(), 1);

        메뉴_후라이드_리퀘스트 = new MenuRequest("후라이드",
            BigDecimal.valueOf(16000),
            메뉴그룹_한마리메뉴_리퀘스트.getId(),
            Arrays.asList(메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트));
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() {
        //when
        MenuResponse createdMenu = menuService.create(메뉴_후라이드_리퀘스트);
        //then
        assertThat(createdMenu.getName()).isEqualTo(메뉴_후라이드.getName());
    }

    @Test
    @DisplayName("메뉴가격이 0보다 작거나 비어있는경우 메뉴 생성을 실패한다.")
    void create_with_exception_when_price_smaller_than_zero_or_null() {
        assertAll(() -> {
                //given
                메뉴_후라이드_리퀘스트 = new MenuRequest("후라이드", null, 메뉴그룹_한마리메뉴_리퀘스트.getId(),
                    Arrays.asList(메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트));

                //when && then
                assertThatThrownBy(() -> menuService.create(메뉴_후라이드_리퀘스트))
                    .isInstanceOf(IllegalArgumentException.class);
            }, () -> {
                //given
                메뉴_후라이드_리퀘스트 = new MenuRequest("후라이드", BigDecimal.valueOf(-1), 메뉴그룹_한마리메뉴_리퀘스트.getId(),
                    Arrays.asList(메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트));

                //when && then
                assertThatThrownBy(() -> menuService.create(메뉴_후라이드_리퀘스트))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        );
    }

    @Test
    @DisplayName("메뉴 그룹이 없는 경우 메뉴 생성을 실패한다.")
    void create_with_exception_when_menu_group_is_null() {
        //given
        메뉴_후라이드_리퀘스트 = new MenuRequest("후라이드",
            BigDecimal.valueOf(16000),
            999L,
            Arrays.asList(메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트));

        //when && then
        assertThatThrownBy(() -> menuService.create(메뉴_후라이드_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 포함된 상품의 가격합보다 큰 경우 메뉴 생성을 실패한다.")
    void create_with_exception_when_menu_price_greater_than_sum_of_product() {
        //given
        메뉴_후라이드_리퀘스트 = new MenuRequest("후라이드",
            BigDecimal.valueOf(99999),
            메뉴그룹_한마리메뉴_리퀘스트.getId(),
            Arrays.asList(메뉴프로덕트_후라이드치킨_후라이드치킨_리퀘스트));

        //when && then
        assertThatThrownBy(() -> menuService.create(메뉴_후라이드_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        //when
        List<MenuResponse> menus = menuService.list();

        //then
        assertThat(menus.size()).isGreaterThan(0);
    }
}