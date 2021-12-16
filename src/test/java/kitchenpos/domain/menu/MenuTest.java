package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.application.menu.MenuValidator;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.exception.NegativePriceException;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class MenuTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuValidator menuValidator;

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Product 공기밥;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);

        고기_메뉴그룹 = menuGroupRepository.save(고기_메뉴그룹);
        돼지고기 = productRepository.save(돼지고기);
        공기밥 = productRepository.save(공기밥);
    }

    @DisplayName("Menu 는 Name, Price, MenuGroup, MenuProducts 로 생성된다.")
    @Test
    void create1() {
        // given
        BigDecimal price = BigDecimal.valueOf(9_000);

        // when
        Menu menu = Menu.of("불고기", price, 고기_메뉴그룹.getId());
        List<MenuProduct> menuProducts = Arrays.asList(MenuProduct.of(돼지고기.getId(), 1L),
                                                       MenuProduct.of(공기밥.getId(), 1L));
        menu.addMenuProducts(menuProducts);

        // then
        assertAll(
            () -> assertEquals(menu.getName(), Name.from("불고기")),
            () -> assertEquals(menu.getMenuGroupId(), 고기_메뉴그룹.getId()),
            () -> assertEquals(menu.getPrice(), Price.from(price))
        );
    }

    @DisplayName("Menu 생성 시, MenuGroup 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        Menu 불고기 = Menu.of("불고기", BigDecimal.valueOf(9_000), 0L);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> menuValidator.validate(불고기));
    }

    @DisplayName("Menu 생성 시, Price 가 null 이면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void create2(BigDecimal price) {
        // when & then
        assertThrows(NegativePriceException.class, () -> Menu.of("불고기", price, 고기_메뉴그룹.getId()));
    }

    @DisplayName("Menu 의 가격은 구성하는 상품의 총 가격의 합보다 크면 예외가 발생한다.")
    @Test
    void validateMenuPrice() {
        // given
        BigDecimal price = BigDecimal.valueOf(1_000_000);
        Menu menu = Menu.of("불고기", price, 고기_메뉴그룹.getId());
        List<MenuProduct> menuProducts = Arrays.asList(MenuProduct.of(돼지고기.getId(), 1L),
                                                       MenuProduct.of(공기밥.getId(), 1L));
        menu.addMenuProducts(menuProducts);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuValidator.validate(menu))
                                            .withMessageContaining("Menu Price 는 상품 가격 총합보다 작아야합니다.");
    }
}