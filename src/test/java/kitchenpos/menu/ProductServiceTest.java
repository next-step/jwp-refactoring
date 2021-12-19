package kitchenpos.menu;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
  @InjectMocks
  ProductService productService;

  @Mock
  ProductDao productDao;

  private Product 치킨;

  @BeforeEach
  void setUp() {
    치킨 = MenuFactory.ofProduct("치킨", 3500);
  }

  @DisplayName("상품을 생성한다.")
  @Test
  void 상품_생성() {
    // given
    Product expected = MenuFactory.ofProduct(1L, "치킨", 3500);
    given(productDao.save(치킨)).willReturn(expected);

    // when
    Product response = productService.create(치킨);

    // then
    assertAll(
            () -> assertThat(response.getId()).isEqualTo(expected.getId()),
            () -> assertThat(response.getName()).isEqualTo(치킨.getName()),
            () -> assertThat(response.getPrice()).isEqualTo(치킨.getPrice())
    );
  }

  @DisplayName("상품 생성 시 가격이 0원보다 낮으면 안된다.")
  @Test
  void 상품_생성_가격_0원_미만_예외() {
    // given
    치킨.setPrice(BigDecimal.valueOf(-1));

    // when
    Throwable thrown = catchThrowable(() -> productService.create(치킨));

    // then
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("상품 생성 시 가격이 null 값이 입력되면 안된다.")
  @Test
  void 상품_생성_가격_null_예외() {
    // given
    치킨.setPrice(null);

    // when
    Throwable thrown = catchThrowable(() -> productService.create(치킨));

    // then
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("상품 목록을 조회한다.")
  @Test
  void 상품_목록_조회() {
    // given
    Product 치킨_예상결과 = MenuFactory.ofProduct(1L, "치킨", 3500);
    Product 콜라_예상결과 = MenuFactory.ofProduct(2L, "콜라", 500);
    given(productDao.findAll()).willReturn(Arrays.asList(치킨_예상결과, 콜라_예상결과));

    // when
    List<Product> response = productService.list();

    // then
    assertThat(response).containsExactly(치킨_예상결과, 콜라_예상결과);
  }
}
