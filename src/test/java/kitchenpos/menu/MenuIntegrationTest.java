package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
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

@SpringBootTest
@Transactional
public class MenuIntegrationTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuService menuService;

    private MenuGroup 메뉴그룹;
    private Product 제품1;
    private Product 제품2;

    @BeforeEach
    void setUp() {
        메뉴그룹 = menuGroupRepository.save(new MenuGroup("테스트메뉴그룹"));
        제품1 = productRepository.save(new Product("테스트제품1", BigDecimal.valueOf(1000L)));
        제품2 = productRepository.save(new Product("테스트제품2", BigDecimal.valueOf(3000L)));

        Menu 메뉴1 = Menu.Builder.of("테스트메뉴1", BigDecimal.valueOf(3000L))
                               .menuGroup(메뉴그룹)
                               .menuProducts(Arrays.asList(new MenuProduct(제품1, 2),
                                                           new MenuProduct(제품2, 4)))
                               .build();

        Menu 메뉴2 = Menu.Builder.of("테스트메뉴2", BigDecimal.valueOf(5000L))
                               .menuGroup(메뉴그룹)
                               .menuProducts(Arrays.asList(new MenuProduct(제품1, 2),
                                                           new MenuProduct(제품2, 4)))
                               .build();

        menuRepository.save(메뉴1);
        menuRepository.save(메뉴2);
    }


    @DisplayName("메뉴 생성 통합 테스트")
    @Test
    void createTest() {
        MenuRequest menuRequest = MenuRequest.Builder.of("맛좋은테스트메뉴", BigDecimal.valueOf(2000L))
                                                     .menuGroupId(메뉴그룹.getId())
                                                     .menuProducts(Arrays.asList(new MenuProductRequest(제품1.getId(), 2),
                                                                                 new MenuProductRequest(제품2.getId(), 4)))
                                                     .build();

        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        assertAll(() -> {
            assertThat(menuResponse).isNotNull();
            assertThat(menuResponse.getName()).isEqualTo("맛좋은테스트메뉴");
            assertThat(menuResponse.getPrice()).isEqualTo(BigDecimal.valueOf(2000L));
            assertThat(menuResponse.getMenuGroupId()).isEqualTo(메뉴그룹.getId());
            assertThat(menuResponse.getMenuProducts()).hasSize(2);
        });
    }

    @DisplayName("전체 메뉴 조회 통합 테스트")
    @Test
    void listTest() {
        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSizeGreaterThanOrEqualTo(2);
    }


}
