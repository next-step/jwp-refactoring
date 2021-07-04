package kitchenpos.product.application;

import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.application.ProductService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private ProductDao productDao;

  @DisplayName("상품의 이름과 가격을 입력받아 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    Product product = new Product("상품", BigDecimal.valueOf(1_000));
    when(productDao.save(any())).thenReturn(new Product(1L, "상품", BigDecimal.valueOf(1_000)));
    ProductService productService = new ProductService(productDao);

    //when
    Product savedProduct = productService.create(product);

    //then
    assertAll(() -> assertThat(savedProduct.getId()).isNotNull(),
              () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
              () -> assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice()));
    verify(productDao, VerificationModeFactory.times(1)).save(product);
  }

  @DisplayName("상품의 가격은 0 이상이어야 한다.")
  @Test
  void createFailTest() {
    //given
    Product nullPriceProduct = new Product("상품", null);
    Product negativePriceProduct = new Product("상품", BigDecimal.valueOf(-1_000));
    ProductService productService = new ProductService(productDao);

    //when & then
    assertAll(() -> assertThatThrownBy(() -> productService.create(nullPriceProduct)).isInstanceOf(IllegalArgumentException.class),
              () -> assertThatThrownBy(() -> productService.create(negativePriceProduct)).isInstanceOf(IllegalArgumentException.class));
  }

  @DisplayName("상품 목록을 반환한다.")
  @Test
  void findAllTest() {
    //given
    Product product1 = new Product("상품1", BigDecimal.valueOf(1_000));
    Product product2 = new Product("상품2", BigDecimal.valueOf(2_000));
    Product product3 = new Product("상품3", BigDecimal.valueOf(3_000));
    when(productDao.findAll()).thenReturn(Arrays.asList(product1, product2, product3));
    ProductService productService = new ProductService(productDao);

    //when
    List<Product> productList = productService.list();

    //then
    assertThat(productList).containsExactly(product1, product2, product3);
  }


}
