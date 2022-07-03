package kitchenpos.menu.validator;

import static kitchenpos.utils.DomainFixtureFactory.createMenuGroup;
import static kitchenpos.utils.DomainFixtureFactory.createMenuRequest;
import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private MenuGroupService menuGroupService;
    @Mock
    private ProductService productService;
    @InjectMocks
    private MenuValidator menuValidator;
    private MenuGroup 한마리메뉴;
    private Product 양념;

    @BeforeEach
    void setUp() {
        양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        한마리메뉴 = createMenuGroup(1L, "한마리메뉴");
    }

    @DisplayName("메뉴그룹 null 경우 테스트")
    @Test
    void validateMenuGroup() {
        MenuRequest menuRequest = createMenuRequest("양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴.id(),
                Lists.newArrayList(new MenuProductRequest(양념.id(), 2L)));
        given(menuGroupService.findMenuGroup(한마리메뉴.id())).willReturn(null);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.validate(menuRequest))
                .withMessage("메뉴그룹이 있어야 합니다.");
    }

    @DisplayName("메뉴 가격은 상품의 총 금액 비교 테스트")
    @Test
    void createWithTotalPriceLessThanMenuPrice() {
        MenuRequest menuRequest = createMenuRequest("양념치킨", BigDecimal.valueOf(60000L), 한마리메뉴.id(),
                Lists.newArrayList(new MenuProductRequest(양념.id(), 2L)));
        given(menuGroupService.findMenuGroup(한마리메뉴.id())).willReturn(한마리메뉴);
        given(productService.findProduct(양념.id())).willReturn(양념);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.validate(menuRequest))
                .withMessage("메뉴 가격은 상품의 총 금액을 넘길 수 없습니다.");
    }
}
