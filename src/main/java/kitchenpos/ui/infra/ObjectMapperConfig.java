package kitchenpos.ui.infra;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import kitchenpos.domain.Money;

@Configuration
public class ObjectMapperConfig {

	@Bean
	public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
		return Jackson2ObjectMapperBuilder.json()
				.modules(getMoneyModule());
	}

	private List<Module> getMoneyModule() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(Money.class, new MoneySerializer());
		return Collections.singletonList(module);
	}
}
