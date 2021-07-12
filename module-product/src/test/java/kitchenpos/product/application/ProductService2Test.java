package kitchenpos.product.application;

import kitchenpos.product.domain.ProductEntity;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductService2Test {

  @Mock
  private ProductRepository productRepository;

  @DisplayName("상품의 이름과 가격을 입력받아 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    ProductRequest newRequest = new ProductRequest("상품", 1_000D);
    when(productRepository.save(any())).thenReturn(new ProductEntity(1L, "상품", 1_000D));
    ProductService2 productService = new ProductService2(productRepository);

    //when
    ProductResponse savedProduct = productService.create(newRequest);

    //then
    assertAll(() -> assertThat(savedProduct.getId()).isNotNull(),
        () -> assertThat(savedProduct.getName()).isEqualTo(newRequest.getName()),
        () -> assertThat(savedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(newRequest.getPrice())));
    verify(productRepository, VerificationModeFactory.times(1)).save(any());
  }

  @DisplayName("상품의 가격은 0 이상이어야 한다.")
  @Test
  void createFailTest() {
    //given
    ProductRequest nullPriceProduct = new ProductRequest("상품", null);
    ProductRequest negativePriceProduct = new ProductRequest("상품",-1_000D);
    ProductService2 productService = new ProductService2(productRepository);

    //when & then
    assertAll(() -> assertThatThrownBy(() -> productService.create(nullPriceProduct)).isInstanceOf(IllegalArgumentException.class),
        () -> assertThatThrownBy(() -> productService.create(negativePriceProduct)).isInstanceOf(IllegalArgumentException.class));
  }

  @DisplayName("상품 목록을 반환한다.")
  @Test
  void findAllTest() {
    //given
    ProductEntity product1 = new ProductEntity("상품1", 1_000D);
    ProductEntity product2 = new ProductEntity("상품2", 2_000D);
    ProductEntity product3 = new ProductEntity("상품3", 3_000D);
    when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2, product3));
    ProductService2 productService = new ProductService2(productRepository);

    //when
    List<ProductResponse> productList = productService.findAllProducts();

    //then
    Assertions.assertThat(productList).isEqualTo(ProductResponse.ofList(Arrays.asList(product1, product2, product3)));
  }

}
