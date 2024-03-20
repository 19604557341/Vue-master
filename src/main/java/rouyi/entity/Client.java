package rouyi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Client {
    @TableId( type = IdType.AUTO)
    private Integer clientId;
    private String userName;
    private String phone;
    private String password;
    private Integer status;
    private Integer integral;
}
