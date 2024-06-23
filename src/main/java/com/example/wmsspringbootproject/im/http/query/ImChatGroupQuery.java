package com.example.wmsspringbootproject.im.http.query;

import com.example.wmsspringbootproject.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ImChatGroupQuery extends BasePageQuery {

    @Schema(description = "搜索关键字")
    private String keyWords;
}
