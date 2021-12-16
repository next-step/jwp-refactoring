package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.exception.NegativePriceException;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class MenuServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Product 공기밥;
    private Menu 불고기;
    private MenuProduct 불고기_돼지고기;
    private MenuProduct 불고기_공기밥;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);
        불고기 = MenuFixtureFactory.create(1L, "불고기", 10_000, 고기_메뉴그룹);

        불고기_돼지고기 = MenuProductFixtureFactory.create(1L, 불고기, 돼지고기, 1L);
        불고기_공기밥 = MenuProductFixtureFactory.create(2L, 불고기, 공기밥, 1L);
        불고기.addMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        고기_메뉴그룹 = menuGroupRepository.save(고기_메뉴그룹);
        돼지고기 = productRepository.save(돼지고기);
        공기밥 = productRepository.save(공기밥);
        불고기 = menuRepository.save(불고기);
    }

    @DisplayName("Menu 를 등록한다.")
    @Test
    void create1() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(불고기_돼지고기.getProductId(), 불고기_돼지고기.getQuantity().getValue()),
                          MenuProductRequest.of(불고기_공기밥.getProductId(), 불고기_공기밥.getQuantity().getValue()));

        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  BigDecimal.valueOf(10_000),
                                                  고기_메뉴그룹.getId(),
                                                  menuProductRequests);
        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        Menu findMenu = menuRepository.findById(menuResponse.getId()).get();
        assertThat(menuResponse).isEqualTo(MenuResponse.from(findMenu));
    }

    @DisplayName("Menu 가격은 null 이면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(불고기_돼지고기.getProductId(), 불고기_돼지고기.getQuantity().getValue()),
                          MenuProductRequest.of(불고기_공기밥.getProductId(), 불고기_공기밥.getQuantity().getValue()));
        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  null,
                                                  고기_메뉴그룹.getId(),
                                                  menuProductRequests);
        // when & then
        assertThrows(NegativePriceException.class, () -> menuService.create(menuRequest));
    }

    @DisplayName("Menu 가격은 음수(0원 미만)이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void create3(int wrongPrice) {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(불고기_돼지고기.getProductId(), 불고기_돼지고기.getQuantity().getValue()),
                          MenuProductRequest.of(불고기_공기밥.getProductId(), 불고기_공기밥.getQuantity().getValue()));
        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  BigDecimal.valueOf(wrongPrice),
                                                  고기_메뉴그룹.getId(),
                                                  menuProductRequests);
        // when & then
        assertThrows(NegativePriceException.class, () -> menuService.create(menuRequest));
    }

    @DisplayName("Menu 는 자신이 속할 MenuGroup 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(불고기_돼지고기.getProductId(), 불고기_돼지고기.getQuantity().getValue()),
                          MenuProductRequest.of(불고기_공기밥.getProductId(), 불고기_공기밥.getQuantity().getValue()));
        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  BigDecimal.valueOf(10_000),
                                                  0L,
                                                  menuProductRequests);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> menuService.create(menuRequest));
    }

    @DisplayName("Menu 는 메뉴를 구성하는 Product 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(0L, 불고기_돼지고기.getQuantity().getValue()),
                          MenuProductRequest.of(0L, 불고기_공기밥.getQuantity().getValue()));
        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  BigDecimal.valueOf(10_000),
                                                  고기_메뉴그룹.getId(),
                                                  menuProductRequests);
        // when & then
        assertThrows(EntityNotFoundException.class, () -> menuService.create(menuRequest));
    }

    @DisplayName("Menu 의 총 가격이 메뉴를 구성하는 각 상품의 (가격 * 수량) 총합보다 크면 예외가 발생한다.")
    @Test
    void create6() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(불고기_돼지고기.getProductId(), 불고기_돼지고기.getQuantity().getValue()),
                          MenuProductRequest.of(불고기_공기밥.getProductId(), 불고기_공기밥.getQuantity().getValue()));
        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  BigDecimal.valueOf(1000_000),
                                                  고기_메뉴그룹.getId(),
                                                  menuProductRequests);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("Menu 목록을 조회한다.")
    @Test
    void findList() {
        // when
        List<MenuResponse> menuResponses = menuService.list();

        // then
        assertThat(menuResponses).contains(MenuResponse.from(불고기));
    }
}