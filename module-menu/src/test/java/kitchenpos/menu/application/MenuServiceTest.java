package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.application.fixture.MenuProductDtoFixtureFactory;
import kitchenpos.menu.application.behavior.MenuContextServiceBehavior;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.product.application.behavior.ProductContextServiceBehavior;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

class MenuServiceTest extends ServiceTest {
    @Autowired
    private MenuContextServiceBehavior menuContextServiceBehavior;

    @Autowired
    private ProductContextServiceBehavior productContextServiceBehavior;

    @Autowired
    private MenuService menuService;

    private MenuGroup menuGroup;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        menuGroup = menuContextServiceBehavior.메뉴그룹_생성됨("메뉴그룹1");
        product1 = productContextServiceBehavior.상품_생성됨("상품1", 1000);
        product2 = productContextServiceBehavior.상품_생성됨("상품2", 2000);
    }

    @Test
    @DisplayName("메뉴 등록")
    void 메뉴그룹에_메뉴추가() {
        String menuName = "메뉴1";
        int menuPrice = 5000;
        MenuDto savedMenu = 테스트_메뉴_생성(menuGroup, menuName, menuPrice);

        Assertions.assertAll("저장된 메뉴를 확인한다"
                , () -> assertThat(savedMenu.getMenuGroup().getId()).isEqualTo(menuGroup.getId())
                , () -> assertThat(savedMenu.getId()).isNotNull()
                , () -> assertThat(savedMenu.getName()).isEqualTo(menuName)
                , () -> assertThat(savedMenu.getPrice().intValue()).isEqualTo(menuPrice)
        );

        List<MenuProductDto> menuProducts = savedMenu.getMenuProductDtos();
        List<Long> menuProductIds = menuProducts.stream()
                .map(MenuProductDto::getProductId)
                .collect(toList());
        Assertions.assertAll("저장된 메뉴의 메뉴 상품을 확인한다"
                , () -> assertThat(menuProducts).hasSize(2)
                , () -> assertThat(menuProductIds).containsExactlyInAnyOrder(product1.getId(), product2.getId())
        );
    }

    @Test
    @DisplayName("메뉴그룹이 존재하지 않을 경우 메뉴 등록 실패")
    void 메뉴그룹에_메뉴추가_존재하지않는_메뉴그룹의_경우() {
        MenuGroup notSavedMenuGroup = new MenuGroup("메뉴그룹1");
        String menuName = "메뉴1";
        int menuPrice = 5000;
        assertThatThrownBy(() -> 테스트_메뉴_생성(notSavedMenuGroup, menuName, menuPrice))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("메뉴가격이 음수인 경우 메뉴 등록 실패")
    void 메뉴그룹에_메뉴추가_가격이_음수인경우() {
        String menuName = "메뉴1";
        int menuPrice = -1 * 1000;
        assertThatThrownBy(() -> 테스트_메뉴_생성(menuGroup, menuName, menuPrice))
                .isInstanceOf(InvalidMenuPriceException.class);
    }

    @Test
    @DisplayName("메뉴가격이 상품가격의 합보다 큰 경우 메뉴 등록 실패")
    void 메뉴그룹에_메뉴추가_가격이_상품가격의_합보다_큰경우() {
        String menuName = "메뉴1";
        int menuPrice = 10000;
        assertThatThrownBy(() -> 테스트_메뉴_생성(menuGroup, menuName, menuPrice))
                .isInstanceOf(InvalidMenuPriceException.class);
    }

    @Test
    @DisplayName("메뉴 목록 조회")
    void 메뉴목록_조회() {
        테스트_메뉴_생성(menuGroup, "menu1", 6000);
        테스트_메뉴_생성(menuGroup, "menu2", 5000);
        List<MenuDto> menus = menuService.list();
        assertThat(menus).hasSize(2);
    }

    private MenuDto 테스트_메뉴_생성(MenuGroup menuGroup, String menuName, int menuPrice) {
        MenuProductDto menuProduct1 = MenuProductDtoFixtureFactory.createMenuProduct(product1.getId(), 4);
        MenuProductDto menuProduct2 = MenuProductDtoFixtureFactory.createMenuProduct(product2.getId(), 1);
        return menuContextServiceBehavior.메뉴_생성됨(menuGroup, menuName, menuPrice,
                Lists.newArrayList(menuProduct1, menuProduct2));
    }

}
