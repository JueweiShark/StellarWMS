package com.example.wmsspringbootproject.common.handle;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.example.wmsspringbootproject.Utils.SecurityUtils;
import com.example.wmsspringbootproject.common.Annotation.DataPermission;
import com.example.wmsspringbootproject.common.base.IBaseEnum;
import com.example.wmsspringbootproject.constants.Constants;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 数据权限控制器
 *
 */
@Slf4j
public class MyDataPermissionHandler implements DataPermissionHandler {

    /**
     *
     * @param where             待执行 SQL Where 条件表达式
     * @param mappedStatementId Mybatis MappedStatement Id 根据该参数可以判断具体执行方法
     * @return 过滤后的 SQL Where 条件表达式
     */
    @Override
    @SneakyThrows
    public Expression getSqlSegment(Expression where, String mappedStatementId) {

        Class<?> clazz = Class.forName(mappedStatementId.substring(0, mappedStatementId.lastIndexOf(StringPool.DOT)));
        String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(StringPool.DOT) + 1);
        Method[] methods =DataPermissionAspect.getMethods().toArray(new Method[0]);
//        if(methods.length==0){
//            methods=DataPermissionAspect.getMethods().toArray(new Method[0]);
//        }
        for (Method method : methods) {
            DataPermission annotation = method.getAnnotation(DataPermission.class);
            // 如果没有注解或者是超级管理员，直接返回
            if (annotation == null || SecurityUtils.isRoot()||SecurityUtils.isAdmin()) {
                return where;
            }
            if (method.getName().equals(methodName) || (method.getName() + "_COUNT").equals(methodName) || annotation.value()) {
                return dataScopeFilter(annotation.warehouseAlias(), annotation.warehouseIdColumnName(), annotation.userAlias(), annotation.userIdColumnName(), where);
            }
        }
        return where;
    }


    /**
     * 数据权限过滤器
     * @param warehouseAlias 仓库别名
     * @param warehouseIdColumnName 仓库 ID 列名
     * @param userAlias 用户别名
     * @param userIdColumnName 用户 ID 列名
     * @param where 待执行 SQL Where 条件表达式
     * @return 过滤后的 SQL Where 条件表达式
     */
    @SneakyThrows
    public static Expression dataScopeFilter(String warehouseAlias, String warehouseIdColumnName, String userAlias, String userIdColumnName, Expression where) {


        String warehouseColumnName = StrUtil.isNotBlank(warehouseAlias) ? (warehouseAlias + StringPool.DOT + warehouseIdColumnName) : warehouseIdColumnName;
        String userColumnName = StrUtil.isNotBlank(userAlias) ? (userAlias + StringPool.DOT + userIdColumnName) : userIdColumnName;

        // 获取当前用户的数据权限
        Integer dataScope = SecurityUtils.getDataScope();

        if(dataScope==null){
            ServletRequestAttributes requestAttributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response=requestAttributes.getResponse();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"用户没有操作权限");
            throw new IllegalArgumentException("用户没有操作权限");
        }

        Constants.DataScopeType dataScopeEnum = IBaseEnum.getEnumByValue(dataScope,Constants.DataScopeType.class);
        
        String warehouseId;
        Long userId;
        String appendSqlStr=null;
        switch (dataScopeEnum) {
            case ALL:
                return where;
            case MANAGED:
                if(SecurityUtils.getWarehouseId().lastIndexOf(",")>0){
                    warehouseId = SecurityUtils.getWarehouseId();
                    appendSqlStr = warehouseColumnName + " IN " +StringPool.LEFT_BRACKET+SecurityUtils.getWarehouseId()+StringPool.RIGHT_BRACKET;
                }else{
                    appendSqlStr=warehouseColumnName+StringPool.EQUALS+SecurityUtils.getWarehouseId();
                }
                break;
            case BELONG:
                userId = SecurityUtils.getUserId();
                appendSqlStr = userColumnName + StringPool.EQUALS + userId;
                break;
        }

        if (StrUtil.isBlank(appendSqlStr)) {
            return where;
        }

        Expression appendExpression = CCJSqlParserUtil.parseCondExpression(appendSqlStr);

        if (where == null) {
            return appendExpression;
        }

        return new AndExpression(where, appendExpression);
    }


}

