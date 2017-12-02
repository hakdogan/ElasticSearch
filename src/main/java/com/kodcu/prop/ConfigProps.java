package com.kodcu.prop;
/*
 * Created by hakdogan on 28/11/2017
 */

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
@Getter
@Setter
public class ConfigProps {

    @NestedConfigurationProperty
    private RestClient restClient = new RestClient();

    @NestedConfigurationProperty
    private Index index = new Index();
}
