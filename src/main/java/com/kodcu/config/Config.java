package com.kodcu.config;

/*
 * Created by hakdogan on 28/11/2017
 */

import com.google.gson.Gson;
import com.kodcu.prop.ConfigProps;
import fr.pilato.elasticsearch.containers.ElasticsearchContainer;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Profile;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Configuration
public class Config {

    @Autowired
    private ConfigProps props;

    @Profile("production")
    @Bean(destroyMethod = "close")
    @SuppressWarnings("all")
    public TransportClient getTransportClient() throws UnknownHostException {
        return new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(props.getClients().getHostname()),
                            props.getClients().getTransportPort()));
    }

    @Profile("production")
    @Bean(destroyMethod = "close")
    public RestHighLevelClient getRestClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(props.getClients().getHostname(),
                props.getClients().getHttpPort(), props.getClients().getScheme())));
    }

    @Profile("test")
    @Bean(destroyMethod = "close")
    public RestHighLevelClient getRestClientForTest() {

        ElasticsearchContainer container = new ElasticsearchContainer();
        container.start();

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(props.getClients().getCredentialUsername(), props.getClients().getCredentialPassword()));
        return new RestHighLevelClient(RestClient.builder(container.getHost())
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)));
    }

    @Bean
    public SearchSourceBuilder getSearchSourceBuilder(){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(props.getIndex().getFrom());
        sourceBuilder.size(props.getIndex().getSize());
        sourceBuilder.timeout(new TimeValue(props.getIndex().getTimeout(), TimeUnit.SECONDS));

        return sourceBuilder;
    }

    @Bean
    public Gson getGson(){
        return new Gson();
    }

}
