package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    //给方法加入注解
    //MyBatis 中的 @Param 注解用于给参数指定别名，以便在 XML 映射文件中引用。
    // 在这里，ew 代表传入的 QueryWrapper<User>，它会在 SQL 中替换为条件片段（customSqlSegment）。
    //@Param("amount"): amount 参数代表减少的金额，会直接替换到 SQL 语句中的 #{amount} 部分。
    void updateBalanceByIds(@Param("ew") QueryWrapper<User> wrapper, @Param("amount") int amount);

    @Update("update user set balance = balance - #{money} where id = #{id}")
    void deductBalance(@Param("id") Long id, @Param("money") Integer money);
}
