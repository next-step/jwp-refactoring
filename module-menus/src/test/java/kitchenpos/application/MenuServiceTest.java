package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.ServiceTest;
import kitchenpos.menus.menu.application.MenuService;
import kitchenpos.menus.menu.domain.Menu;
import kitchenpos.menus.menu.domain.MenuProduct;
import kitchenpos.menus.menu.domain.MenuProductRepository;
import kitchenpos.menus.menu.domain.MenuRepository;
import kitchenpos.menus.menu.dto.MenuProductRequest;
import kitchenpos.menus.menu.dto.MenuRequest;
import kitchenpos.menus.menu.dto.MenuResponse;
import kitchenpos.menus.menugroup.domain.MenuGroup;
import kitchenpos.menus.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuService menuService;

    private Menu 중식메뉴;
    private MenuGroup 중식;
    private MenuProduct 중식_메뉴_짬뽕;
    private MenuProduct 중식_메뉴_짜장;
    private Product 짬뽕;
    private Product 짜장;

    @BeforeEach
    void before(@Autowired ProductRepository productRepository) {
        중식 = menuGroupRepository.save(new MenuGroup("중식"));
        중식메뉴 = menuRepository.save(new Menu("메뉴1", BigDecimal.valueOf(3000), 중식.getId()));

        짬뽕 = productRepository.save(new Product("상품1", BigDecimal.valueOf(1000)));
        짜장 = productRepository.save(new Product("상품2", BigDecimal.valueOf(2000)));

        중식_메뉴_짬뽕 = menuProductRepository.save(new MenuProduct(중식메뉴, 짬뽕.getId(), 3));
        중식_메뉴_짜장 = menuProductRepository.save(new MenuProduct(중식메뉴, 짜장.getId(), 1));

        중식메뉴.addMenuProduct(Arrays.asList(중식_메뉴_짬뽕, 중식_메뉴_짜장));
    }

    @Test
    @DisplayName("생성 하려는 메뉴의 메뉴 그룹이 시스템에 존재 하지 않으면 추가 할 수 없다.")
    void createTestFailWithMenuGroupNotExist() {

        //given
        MenuGroup wrongMenuGroup = new MenuGroup(1000L, "wrong menu group");
        Menu wrongMenu = new Menu("wrong menu", BigDecimal.valueOf(1000), wrongMenuGroup.getId());

        //when & then
        assertThatThrownBy(
                () -> menuService.create(MenuRequest.from(wrongMenu))
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("생성 하려는 메뉴의 메뉴 상품이 시스템에 등록 되어 있지 않으면 추가 할 수 없다.")
    void createTestFailWithMenuProductNotExist() {
        //given
        Product 잘못된_상품 = new Product(45L, "잘못된 상품", BigDecimal.valueOf(10));
        MenuProduct 잘못된_메뉴_상품 = new MenuProduct(중식메뉴, 잘못된_상품.getId(), 10);

        //when & then
        assertThatThrownBy(
                () -> menuService.create(MenuRequest.of(
                        중식메뉴.getName(),
                        중식메뉴.getPrice().longValue(),
                        중식.getId(),
                        Arrays.asList(
                                MenuProductRequest.of(잘못된_상품.getId(), 잘못된_메뉴_상품.getQuantity())
                        )
                ))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성 하려는 메뉴 가격이 전체 메뉴상품의 전체 금액(가격 * 수량의 총합)보다 클 수 없다.")
    void createTestFailWithAmount() {
        //given;
        Menu 잘못된_메뉴 = new Menu("잘못된 메뉴", BigDecimal.valueOf(100_000), 중식.getId());

        //when & then
        assertThatThrownBy(
                () -> menuService.create(MenuRequest.of(
                        잘못된_메뉴.getName(),
                        잘못된_메뉴.getPrice().longValue(),
                        중식.getId(),
                        Arrays.asList(
                                MenuProductRequest.of(짬뽕.getId(), 중식_메뉴_짬뽕.getQuantity()),
                                MenuProductRequest.of(짜장.getId(), 중식_메뉴_짜장.getQuantity()))))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 생성 할 수 있다.")
    void createTest() {
        //when
        MenuResponse menu = menuService.create(
                MenuRequest.of(
                        중식메뉴.getName(),
                        중식메뉴.getPrice().longValue(),
                        중식.getId(),
                        Arrays.asList(
                                MenuProductRequest.of(짬뽕.getId(), 중식_메뉴_짬뽕.getQuantity()),
                                MenuProductRequest.of(짜장.getId(), 중식_메뉴_짜장.getQuantity())))
        );

        //then
        assertThat(menu).isNotNull();
    }

    @Test
    @DisplayName("메뉴의 목록을 조회 할 수 있다.")
    void listTest() {
        //when
        List<MenuResponse> menus = menuService.list();

        //then
        Assertions.assertThat(menus).isNotNull();
    }
}
