package com.example.wmsspringbootproject.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description ="用户查询对象")
@Data
public class UserQuery {
    @Schema(description="关键字(用户名)")
    private String keyword;
    @Schema(description="类型")
    private String type;
    @Schema(description="用户昵称")
    private String nick_name;
    @Schema(description="电子邮箱号码")
    private String email;
    @Schema(description="电话号码")
    private String phone;
}
