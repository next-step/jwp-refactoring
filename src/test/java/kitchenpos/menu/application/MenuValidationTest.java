package kitchenpos.menu.application;

import static common.MenuGroupFixture.메뉴그룹_한마리;
import static common.MenuProductFixture.양념치킨_1개;
import static common.MenuProductFixture.콜라_1개;
import static common.ProductFixture.*;
import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import common.ProductFixture;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuValidation;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Amount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuValidationTest {

    @Mock
    private ProductService productService;

    @Mock
    private MenuGroupService menuGroupService;

    @InjectMocks
    MenuValidation menuValidation;

    /**
     * 양념치킨 가격 16000 콜라 1개 가격 2000
     */
    @Test
    void 메뉴생성시_등록한_상품의합보다_큰_금액을_입력시_예외() {
        // given
        MenuGroup 메뉴그룹_한마리 = 메뉴그룹_한마리();
        MenuProduct 양념치킨_1개 = 양념치킨_1개();
        MenuProduct 콜라_1개 = 콜라_1개();
        MenuRequest menuRequest = new MenuRequest("양념치킨", 20000L, 메뉴그룹_한마리.getId()
            , asList(new MenuProductRequest(양념치킨_1개.getProductId(), 1L),
            new MenuProductRequest(콜라_1개.getProductId(), 1L)));
        Menu menu = menuRequest.toMenu();
        menu.addMenuProducts(menuRequest.toMenuProducts());


        // mocking
        when(productService.findByIdThrow(anyLong())).thenReturn(양념치킨(),
            콜라());

        // then
        Assertions.assertThatThrownBy(() -> {
            menuValidation.valid(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴그룹_ID가_비어있다면_예외() {
        // given
        MenuProduct 양념치킨_1개 = 양념치킨_1개();
        MenuProduct 콜라_1개 = 콜라_1개();
        MenuRequest menuRequest = new MenuRequest("양념치킨", 20000L, null
            , asList(new MenuProductRequest(양념치킨_1개.getProductId(), 1L),
            new MenuProductRequest(콜라_1개.getProductId(), 1L)));
        Menu menu = menuRequest.toMenu();
        menu.addMenuProducts(menuRequest.toMenuProducts());


        // mocking
        when(menuGroupService.findByIdThrow(any())).thenThrow(new IllegalArgumentException());

        // then
        Assertions.assertThatThrownBy(() -> {
            menuValidation.valid(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
