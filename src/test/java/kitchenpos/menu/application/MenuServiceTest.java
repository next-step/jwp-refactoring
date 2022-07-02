package kitchenpos.menu.application;

import kitchenpos.exception.IllegalPriceException;
import kitchenpos.menugroup.exception.NoSuchMenuGroupException;
import kitchenpos.product.exception.NoSuchProductException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.utils.fixture.MenuFixtureFactory.createMenu;
import static kitchenpos.utils.fixture.MenuGroupFixtureFactory.createMenuGroup;
import static kitchenpos.utils.fixture.MenuProductFixtureFactory.createMenuProduct;
import static kitchenpos.utils.fixture.ProductFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DisplayName("메뉴 Service 테스트")
class MenuServiceTest {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuProductRepository menuProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuService menuService;

    private Menu 메뉴_김치찌개세트;
    private MenuGroup 메뉴그룹_한식;
    private Product 김치찌개;
    private Product 공기밥;
    private MenuProduct 김치찌개세트_김치찌개;
    private MenuProduct 김치찌개세트_공기밥;

    @BeforeEach
    void setUp() {
        메뉴그룹_한식 = menuGroupRepository.save(createMenuGroup("한식메뉴"));
        김치찌개 = productRepository.save(createProduct("김치찌개", 8000));
        공기밥 = productRepository.save(createProduct("공기밥", 1000));

        김치찌개세트_김치찌개 = createMenuProduct(김치찌개, 2);
        김치찌개세트_공기밥 = createMenuProduct(공기밥, 2);
        메뉴_김치찌개세트 = createMenu("김치찌개세트", 15000, 메뉴그룹_한식,
                Arrays.asList(김치찌개세트_김치찌개, 김치찌개세트_공기밥));
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void 메뉴_등록(){
        //given
        MenuRequest 메뉴_김치찌개세트_request = MenuRequest.of(
                메뉴_김치찌개세트.getName(),
                메뉴_김치찌개세트.getPrice(),
                메뉴_김치찌개세트.getMenuGroup().getId(),
                메뉴_김치찌개세트.getMenuProducts().stream().
                        map(menuProduct -> MenuProductRequest.of(menuProduct.getProduct().getId(), (int) menuProduct.getQuantity()))
                        .collect(Collectors.toList())
        );

        //when
        MenuResponse savedMenu = menuService.create(메뉴_김치찌개세트_request);
        //then
        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getMenuProducts().size()).isEqualTo(2)
        );
    }

    @DisplayName("메뉴의 가격은 0 이상이어야 한다")
    @Test
    void 메뉴_등록_가격_검증_0이상(){
        //when
        MenuRequest invalidMenu = MenuRequest.of(
                메뉴_김치찌개세트.getName(),
                -15000,
                메뉴_김치찌개세트.getMenuGroup().getId(),
                메뉴_김치찌개세트.getMenuProducts().stream().
                        map(menuProduct -> MenuProductRequest.of(menuProduct.getProduct().getId(), (int) menuProduct.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(IllegalPriceException.class, () -> menuService.create(invalidMenu));
    }

    @DisplayName("메뉴의 가격은, 메뉴상품의 정가의 합보다 클 수 없다")
    @Test
    void 메뉴_등록_가격_검증_정가이하(){
        //when
        MenuRequest invalidMenu = MenuRequest.of(
                메뉴_김치찌개세트.getName(),
                20000,
                메뉴_김치찌개세트.getMenuGroup().getId(),
                메뉴_김치찌개세트.getMenuProducts().stream().
                        map(menuProduct -> MenuProductRequest.of(menuProduct.getProduct().getId(), (int) menuProduct.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(IllegalPriceException.class, () -> menuService.create(invalidMenu));
    }

    @DisplayName("등록하려는 메뉴그룹이 존재해야 한다")
    @Test
    void 메뉴_등록_메뉴그룹_검증(){
        //when
        MenuRequest invalidMenu = MenuRequest.of(
                메뉴_김치찌개세트.getName(),
                메뉴_김치찌개세트.getPrice(),
                0L,
                메뉴_김치찌개세트.getMenuProducts().stream().
                        map(menuProduct -> MenuProductRequest.of(menuProduct.getProduct().getId(), (int) menuProduct.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(NoSuchMenuGroupException.class, () -> menuService.create(invalidMenu));
    }

    @DisplayName("등록하려는 메뉴상품의 상품이 존재해야 한다")
    @Test
    void 메뉴_등록_메뉴상품_검증(){
        MenuRequest invalidMenu = MenuRequest.of(
                메뉴_김치찌개세트.getName(),
                메뉴_김치찌개세트.getPrice(),
                메뉴_김치찌개세트.getMenuGroup().getId(),
                메뉴_김치찌개세트.getMenuProducts().stream().
                        map(menuProduct -> MenuProductRequest.of(0L, (int) menuProduct.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(NoSuchProductException.class, () -> menuService.create(invalidMenu));
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다")
    @Test
    void 메뉴_목록_조회(){
        //given
        MenuRequest 메뉴_김치찌개세트_request = MenuRequest.of(
                메뉴_김치찌개세트.getName(),
                메뉴_김치찌개세트.getPrice(),
                메뉴_김치찌개세트.getMenuGroup().getId(),
                메뉴_김치찌개세트.getMenuProducts().stream().
                        map(menuProduct -> MenuProductRequest.of(menuProduct.getProduct().getId(), (int) menuProduct.getQuantity()))
                        .collect(Collectors.toList())
        );
        MenuResponse savedMenu = menuService.create(메뉴_김치찌개세트_request);

        //when
        List<MenuResponse> list = menuService.list();

        //then
        assertThat(list).contains(savedMenu);
    }
}