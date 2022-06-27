package kitchenpos.domain.domainService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.dto.MenuProductDTO;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuProductDomainServiceTest {

    private MenuProductDomainService menuProductDomainService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuRequest menuRequest;

    @BeforeEach
    public void init() {
        menuProductDomainService = new MenuProductDomainService(productRepository,
            menuGroupRepository);
        menuRequest = new MenuRequest();
        menuRequest.setName("테스트메뉴");
        menuRequest.setPrice(BigDecimal.valueOf(2000));
        menuRequest.setMenuGroupId(1L);

        MenuProductDTO menuProductDTO_1 = new MenuProductDTO();
        menuProductDTO_1.setProductId(1L);
        menuProductDTO_1.setQuantity(1L);

        MenuProductDTO menuProductDTO_2 = new MenuProductDTO();
        menuProductDTO_2.setProductId(2L);
        menuProductDTO_2.setQuantity(2L);

        menuRequest.setMenuProducts(Arrays.asList(menuProductDTO_1, menuProductDTO_2));
    }

    @Test
    @DisplayName("메뉴그룹이 저장되어있지 않으면 에러가 발생한다.")
    public void saveMenuWithoutMenuGroupThrowError() {
        when(menuGroupRepository.findById(menuRequest.getMenuGroupId()))
            .thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(
            () -> menuProductDomainService.validateComponentForCreateMenu(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 전체 상품의 가격보다 크면 에러가 발생한다.")
    public void saveMenuOverAllPriceThrowError() {
        when(menuGroupRepository.findById(menuRequest.getMenuGroupId()))
            .thenReturn(Optional.of(new MenuGroup("테스트그룹")));
        when(productRepository.findById(menuRequest.getMenuProducts().get(0).getProductId()))
            .thenReturn(Optional.of(new Product("치킨", BigDecimal.valueOf(500L))));
        when(productRepository.findById(menuRequest.getMenuProducts().get(1).getProductId()))
            .thenReturn(Optional.of(new Product("피자", BigDecimal.valueOf(500L))));

        assertThatThrownBy(
            () -> menuProductDomainService.validateComponentForCreateMenu(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품이 존재하지 않을때 에러가 발생한다.")
    public void saveMenuWithoutPRoductThrowError() {
        when(menuGroupRepository.findById(menuRequest.getMenuGroupId()))
            .thenReturn(Optional.of(new MenuGroup("테스트그룹")));
        when(productRepository.findById(menuRequest.getMenuProducts().get(0).getProductId()))
            .thenReturn(Optional.of(new Product("피자", BigDecimal.valueOf(500L))));

        assertThatThrownBy(
            () -> menuProductDomainService.validateComponentForCreateMenu(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}