package kitchenpos.application.product;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService;

	@DisplayName("상품 생성")
	@Test
	void 상품_생성() {
		Product 빵 = new Product(1L, "빵", BigDecimal.valueOf(1_000));

		given(productDao.save(any())).willReturn(빵);

		Assertions.assertThat(productService.create(빵)).isEqualTo(빵);
	}

	@DisplayName("상품 생성 > 상품의 가격이 비어있거나 0보다 작으면 안됨")
	@Test
	void 상품_생성_상품의_가격이_비어있거나_0보다_작으면_안됨() {
		Product 마이너스_가격_상품 = new Product(1L, "빵", BigDecimal.valueOf(-1));
		Product 비어있는_가격_상품 = new Product(1L, "빵", null);

		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> productService.create(마이너스_가격_상품));
		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> productService.create(비어있는_가격_상품));
	}

	@DisplayName("모든 상품 목록 조회")
	@Test
	void 모든_상품_목록_조회() {
		Product 빵 = new Product(1L, "빵", BigDecimal.valueOf(1_000));
		Product 고기 = new Product(2L, "고기", BigDecimal.valueOf(2_000));

		given(productDao.findAll()).willReturn(Arrays.asList(빵, 고기));

		Assertions.assertThat(productService.list()).containsExactly(빵, 고기);
	}

}
