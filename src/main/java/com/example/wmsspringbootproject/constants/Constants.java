package com.example.wmsspringbootproject.constants;

import com.example.wmsspringbootproject.common.base.IBaseEnum;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public interface Constants {

    Integer TOKEN_EXPIRE=7200;

    Integer EXPIRE=1800;

    String USER_CACHE="user_";

    String TOKEN_HEADER = "Authorization";

    Long  CREATE_TRANSACTION_NOTIFY_ID=0L; //创建事务的 通知消息 的fromId

    Long CONFIRM_TRANSACTION_NOTIFY_ID=-2L; //确认事务的 通知消息 的fromId

    Long TRANSACTION_ISSUE_REPORT=-3L; //事务问题上报的 通知消息 的fromId

    Long TRANSACTION_AUDIT_SUCCESS=-4L; //事务审核完成的 通知消息 的fromId


    interface PageQuery{
        int DEFAULT_PAGE=1;
        int DEFAULT_SIZE=10;
    }

    interface Snowflake {
        long TWEPOCH = 1288834974657L;
        long WORKER_ID_BITS = 5L;
        long DATACENTER_ID_BITS = 5L;
        long SEQUENCE_BITS = 12L;
    }

    interface TimeValueInMillions{
        long SECOND=1000;
        long MIN=60*SECOND;
        long HOUR=60*MIN;
        long DAY=24*HOUR;
        long WEEK=7*DAY;
        long MONTH=30*DAY;
        long YEAR=12*MONTH;
    }


    enum RoleType{
        ROOT(1,"root"),//超级管理员
        ADMIN(2,"admin"),
        MANAGER(3,"manager"),

        EMPLOYEE(4,"employee");
        @Getter
        private Integer value;

        @Getter
        private String type;

        RoleType(Integer value,String type) {
            this.value=value;
            this.type=type;
        }
    }

    enum DataScopeType implements IBaseEnum<Integer> {
        ALL(0,"所有数据"),//管理员可以查看所有数据
        BELONG(1,"本仓库数据"),//仓库内员工
        MANAGED(2,"所管理仓库的数据");//管理的仓库，可能不止一个

        @Getter
        private Integer value;

        @Getter
        private String type;

        DataScopeType(Integer value,String type) {
            this.value=value;
            this.type=type;
        }

        @Override
        public String getLabel() {
            return type;
        }
    }


    interface Default{
        int DELETED=0;

        int PREPARED=1;//已就绪

    }

    interface history{
        /**
         * IP历史记录缓存
         */
         String IP_HISTORY = "ip_history";
         String IP_HISTORY_STATISTICS = "ip_history_statistics";
        String IP_HISTORY_PROVINCE = "ip_history_province";
        String IP_HISTORY_IP = "ip_history_ip";
        String IP_HISTORY_HOUR = "ip_history_hour";
         String IP_HISTORY_COUNT = "ip_history_count";
    }

    interface transactionStatus{
        Integer CREATE_SUCCESS=1; //创建任务 通知仓库管理者、管理员、超级管理员
        Integer ISSUE_REPORT=-1; //问题上报 通知 创建者
        Integer CONFIRM=2; //确认 通知 管理员、超级管理员
        Integer AUDIT_SUCCESS=3; //已审核完成 通知 创建者
    }
}
