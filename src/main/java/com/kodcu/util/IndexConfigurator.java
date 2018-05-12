package com.kodcu.util;

/*
 * Created by hakdogan on 9.05.2018
 */

import com.kodcu.prop.ConfigProps;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
@Component
@Profile("production")
public class IndexConfigurator {

    private final TransportClient transportClient;
    private final ConfigProps props;

    public IndexConfigurator(TransportClient transportClient, ConfigProps props) {
        this.transportClient = transportClient;
        this.props = props;
    }

    @PostConstruct
    private void createIndexWithMapping() {

        IndicesAdminClient indicesAdminClient = transportClient.admin().indices();
        IndicesExistsRequest request = new IndicesExistsRequest(props.getIndex().getName());
        IndicesExistsResponse indicesExistsResponse = indicesAdminClient.exists(request).actionGet();

        if(!indicesExistsResponse.isExists()){

            indicesAdminClient.prepareCreate(props.getIndex().getName())
                    .setSettings(Settings.builder()
                            .put("index.number_of_shards", props.getIndex().getShard())
                            .put("index.number_of_replicas", props.getIndex().getReplica()))
                    .get();

            log.info("Index was created");

            try {
                XContentBuilder builder = jsonBuilder()
                        .startObject()
                            .startObject(props.getIndex().getType())
                                .startObject("properties")
                                    .startObject("id")
                                        .field("type", "text")
                                    .endObject()
                                    .startObject("firstname")
                                    .field("type", "text")
                                    .endObject()
                                    .startObject("lastname")
                                    .field("type", "text")
                                    .endObject()
                                    .startObject("message")
                                    .field("type", "text")
                                    .endObject()
                                .endObject()
                            .endObject()
                        .endObject();

                indicesAdminClient.preparePutMapping(props.getIndex().getName())
                        .setType(props.getIndex().getType())
                        .setSource(builder.string(), XContentType.JSON)
                        .get();
            } catch (IOException ex){
                log.error("The exception was thrown in createIndexWithMapping method. {} ", ex);
            }
        }
    }
}
