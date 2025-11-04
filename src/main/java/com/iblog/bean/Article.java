package com.iblog.bean;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;


/**
 * @author fz
 */
@Data
public class Article {
    private Long id;
    private String title;
    private String mdContent;
    private String htmlContent;
    private String summary;
    private Long cid;
    private Long uid;
    private LocalDateTime publishDate;
    private Integer state;
    private Integer pageView;
    private LocalDateTime editTime;
    private String[] dynamicTags;
    private String nickname;
    private String cateName;
    private List<Tags> tags;
    private String stateStr;
}
