package com.example.wmsspringbootproject.model.query;

import com.example.wmsspringbootproject.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description ="用户查询对象")
@Data
public class UserQuery extends BasePageQuery {
    @Schema(description="关键字(用户名)")
    private String keyword;
    @Schema(description="用户昵称")
    private String nick_name;
    @Schema(description="电子邮箱号码")
    private String email;
    @Schema(description="电话号码")
    private String phone;
    @Schema(description="状态")
    private int status;
    @Schema(description="创建时间")
    private String createTime;
    @Schema(description="结束时间")
    private String endTime;
    @Schema(description="权限id")
    private int typeId;
}
