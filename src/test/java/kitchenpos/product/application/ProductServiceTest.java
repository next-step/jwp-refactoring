package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.MySpringBootTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@DisplayName("금액이 0원 이상인 상품을 등록한다.")
	@Test
	void create() {
		//given
		ProductRequest request = new ProductRequest(
			  "리코타치즈샐러드",
			  new BigDecimal(6_000)
		);

		//when
		ProductResponse response = productService.create(request);

		//then
		List<ProductResponse> list = productService.list();
		assertThat(list).contains(response);
	}

	@DisplayName("금액이 0원미만 상품은 등록할 수 없다.")
	@Test
	void createWithUnderZeroPrice() {
		//given
		ProductRequest request = new ProductRequest(
			  "리코타치즈샐러드",
			  new BigDecimal(-1)
		);

		//when
		상품등록이_실패함(request);

		//given
		request = new ProductRequest(
			  "리코타치즈샐러드",
			  null
		);
		//when
		상품등록이_실패함(request);
	}

	private void 상품등록이_실패함(ProductRequest request) {
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> productService.create(request))
			  .withMessage("상품금액은 0원 이상이어야 합니다.");
	}
}
