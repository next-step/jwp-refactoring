package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.helper.ProductFixtures;
import kitchenpos.menu.domain.Amount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 관련 Domain 단위 테스트")
class ProductTest {

    @DisplayName("금액(가격 * 수량)을 생성한다.")
    @Test
    void createAmount() {
        //given
        int quantity = 10;
        Product product = ProductFixtures.제육덮밥_요청.toProduct();

        //when
        Amount amount = product.createAmount(quantity);

        //then
        assertThat(amount.calculateAmount()).isEqualTo(80_000);

    }

    @DisplayName("상품 가격이 null 이거나 0원 미만일 수 없다.")
    @Test
    void validate() {
        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("제육덮밥", -1));
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("제육덮밥", null));
    }
}
