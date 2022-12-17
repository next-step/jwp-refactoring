package kitchenpos.menu.validator;

import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProductRequest;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenuRequest;
import static kitchenpos.menugroup.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.ProductTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 관련 validator 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    private Product 감자튀김;
    private Product 불고기버거;
    private Product 콜라;
    private MenuGroup 햄버거세트;
    private MenuGroup 햄버거단품;
    private MenuProduct 감자튀김상품;
    private MenuProduct 불고기버거상품;
    private MenuProduct 콜라상품;
    private Menu 불고기버거세트;
    private Menu 불고기버거단품;
    private List<MenuProductRequest> 불고기버거상품요청 = new ArrayList<>();

    @BeforeEach
    void setUp() {
        감자튀김 = ProductTestFixture.generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = ProductTestFixture.generateProduct(2L, "콜라", BigDecimal.valueOf(1500L));
        불고기버거 = ProductTestFixture.generateProduct(3L, "불고기버거", BigDecimal.valueOf(4000L));
        햄버거세트 = generateMenuGroup(1L, "햄버거세트");
        햄버거단품 = generateMenuGroup(2L, "햄버거단품");
        감자튀김상품 = generateMenuProduct(1L, null, 감자튀김, 1L);
        콜라상품 = generateMenuProduct(2L, null, 콜라, 1L);
        불고기버거상품 = generateMenuProduct(3L, null, 불고기버거, 1L);
        불고기버거세트 = generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트,
                Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));
        불고기버거단품 = generateMenu(2L, "불고기버거단품", BigDecimal.valueOf(4000L), 햄버거단품, Collections.singletonList(불고기버거상품));
        불고기버거상품요청.add(generateMenuProductRequest(감자튀김.getId(), 1L));
        불고기버거상품요청.add(generateMenuProductRequest(콜라.getId(), 1L));
        불고기버거상품요청.add(generateMenuProductRequest(불고기버거.getId(), 1L));
    }

    @DisplayName("존재하는 메뉴 그룹에 속하지 않는 메뉴는 생성할 수 없다.")
    @Test
    void validateMenuRequestThrowErrorWhenMenuGroupIsNotExists() {
        // given
        Long notExistsMenuGroupId = 10L;
        MenuRequest menuRequest = generateMenuRequest(불고기버거세트.getName(), BigDecimal.valueOf(8500L), notExistsMenuGroupId, 불고기버거상품요청);
        given(menuGroupRepository.findById(notExistsMenuGroupId)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuValidator.validate(menuRequest))
                .withMessage(ErrorCode.존재하지_않는_메뉴_그룹.getErrorMessage());
    }

    @DisplayName("메뉴 가격이 메뉴 상품들의 가격의 합보다 크면 메뉴를 생성할 수 없다.")
    @Test
    void validateMenuRequestThrowErrorWhenMenuPriceIsHigherThanMenuProductsPrice() {
        // given
        MenuRequest menuRequest = generateMenuRequest(불고기버거세트.getName(), BigDecimal.valueOf(9500L), 햄버거세트.getId(), 불고기버거상품요청);
        given(menuGroupRepository.findById(햄버거세트.getId())).willReturn(Optional.of(햄버거세트));
        given(productRepository.findById(불고기버거.getId())).willReturn(Optional.of(불고기버거));
        given(productRepository.findById(콜라.getId())).willReturn(Optional.of(콜라));
        given(productRepository.findById(감자튀김.getId())).willReturn(Optional.of(감자튀김));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuValidator.validate(menuRequest))
                .withMessage(ErrorCode.메뉴의_가격은_메뉴상품들의_가격의_합보다_클_수_없음.getErrorMessage());
    }

    @DisplayName("존재하지 않는 상품을 가진 메뉴는 생성할 수 없다.")
    @Test
    void validateMenuRequestThrowErrorWhenProductIsNotExists() {
        MenuRequest menuRequest = generateMenuRequest(불고기버거단품.getName(), BigDecimal.valueOf(4000L), 햄버거단품.getId(),
                Collections.singletonList(generateMenuProductRequest(불고기버거.getId(), 1L)));
        given(menuGroupRepository.findById(햄버거단품.getId())).willReturn(Optional.of(햄버거단품));
        given(productRepository.findById(불고기버거.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuValidator.validate(menuRequest))
                .withMessage(ErrorCode.존재하지_않는_상품.getErrorMessage());
    }
}
