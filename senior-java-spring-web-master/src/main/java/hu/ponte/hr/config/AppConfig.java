package hu.ponte.hr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

/**
 * @author zoltan
 */
@Configuration
public class AppConfig
{
	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(Locale.ENGLISH);
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(2097152);  // 2MB
		return multipartResolver;
	}

//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**")
//				.allowedOrigins("*")
//				.allowedMethods("GET", "POST", "DELETE", "PUT")
//				.allowCredentials(true);
//	}
}
