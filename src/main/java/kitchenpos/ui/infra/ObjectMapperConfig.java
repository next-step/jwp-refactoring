package kitchenpos.ui.infra;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import kitchenpos.domain.Money;

@Configuration
public class ObjectMapperConfig {

	@Bean
	public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
		return Jackson2ObjectMapperBuilder.json()
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.modules(getModule());
	}

	private List<Module> getModule() {
		return Arrays.asList(new JavaTimeModule(), getMoneyModule());
	}

	private static SimpleModule getMoneyModule() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(Money.class, new MoneySerializer());
		return module;
	}
}
