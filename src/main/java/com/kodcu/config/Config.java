package com.kodcu.config;

/*
 * Created by hakdogan on 28/11/2017
 */

import com.google.gson.Gson;
import com.kodcu.prop.ConfigProps;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
public class Config {

    @Autowired
    private ConfigProps props;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient getRestClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(props.getRestClient().getHostname(),
                props.getRestClient().getPort(), props.getRestClient().getScheme())));
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
