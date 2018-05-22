package com.kodcu.entity;
/*
 * Created by hakdogan on 01/12/2017
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    private String id;
    private String title;
    private String subject;
    private String content;
}
