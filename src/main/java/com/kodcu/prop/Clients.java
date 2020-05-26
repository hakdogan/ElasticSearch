package com.kodcu.prop;

import lombok.Getter;
import lombok.Setter;

/*
 * Created by hakdogan on 01/12/2017
 */
@Getter
@Setter
public class Clients {
    private String hostname;
    private String scheme;
    private int httpPort;
    private int containerPort;
}
