package kitchenpos.ui.infra;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import kitchenpos.domain.Money;

public class MoneySerializer extends StdSerializer<Money> {

	protected MoneySerializer() {
		super(Money.class);
	}

	@Override
	public void serialize(Money value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeNumber(value.toBigInteger());
	}
}
