package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.TableEmpty;

@DisplayName("테이블 빈 상태 테스트")
class TableEmptyTest {

	@Test
	@DisplayName("테이블 상태 생성")
	void createTableEmptyTest() {
		assertThatNoException()
			.isThrownBy(() -> TableEmpty.from(true));
	}

	@Test
	@DisplayName("빈 값 체크")
	void isEmptyTest() {
		assertThat(TableEmpty.from(true).isEmpty()).isTrue();
		assertThat(TableEmpty.from(false).isEmpty()).isFalse();
	}

	@Test
	@DisplayName("채워있는 값 체크")
	void isNotEmptyTest() {
		assertThat(TableEmpty.from(true).isFull()).isFalse();
		assertThat(TableEmpty.from(false).isFull()).isTrue();
	}
}
