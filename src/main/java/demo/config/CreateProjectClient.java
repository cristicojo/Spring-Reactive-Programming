package demo.config;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.defaultconfig.ApiRootBuilder;
import com.commercetools.api.defaultconfig.ServiceRegion;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("classpath:config.properties")
public class CreateProjectClient {

	@Value("${clientId}")
	private String clientId;

	@Value("${clientSecret}")
	private String clientSecret;

	@Value("${projectKey}")
	private String projectKey;


	@Bean
	public ProjectApiRoot instance() {
		return ApiRootBuilder.of()
				.defaultClient(ClientCredentials.of()
								.withClientId(clientId)
								.withClientSecret(clientSecret)
								.build(),
						ServiceRegion.GCP_EUROPE_WEST1)
				.build(projectKey);
	}
}