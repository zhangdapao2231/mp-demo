package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

@Transactional
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        user.setUsername("Lucy");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", Arrays.asList(1L, 2L, 3L, 4L));  // 使用 in 查询多个 ID

        // 执行查询
        List<User> users = userMapper.selectList(queryWrapper);

        // 输出结果
        users.forEach(System.out::println);
    }


    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(1L);
    }

    @Test
    void a() {
        User user = userMapper.selectById(7L);
        System.out.println(user);
    }


    //该测试方法中id、username等字段都是写死的，属于硬编码，在规约中不太推荐
    @Test
    void testQueryWrapper(){
        //1.构建查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("id","username","info","balance")
                .like("username","o")
                .ge("balance",1000);
        //2.查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    //lambda操作可以解决上述问题
    @Test
    void testLambdaQueryWrapper(){
        //1.构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getId,User::getUsername,User::getInfo,User::getBalance)
                .like(User::getUsername,"o")
                .ge(User::getBalance,1000);
        //2.查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateByQueryWrapper(){
        //要跟新的数据
        User user = new User();
        user.setBalance(2000);
        //更新的条件
        //这里要查询出username为jack的字段，所以建立QueryWrapper
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username","jack");
        //执行更新
        userMapper.update(user,wrapper);
    }

    @Test
    void testUpdateWrapper(){
        List<Long> ids =  Arrays.asList(1L,2L,4L);

        //这里直接进行更新操作，因此建立UpdateWrapper
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200")
                .in("id",ids);
        userMapper.update(null,wrapper);
    }

    @Test
    void testCustomSqlUpdate(){
        //1.更新条件
        List<Long> ids =  Arrays.asList(1L,2L,4L);
        int amount = 200;

        //2.定义条件，指定要更新哪些 ID 的用户
        QueryWrapper<User> wrapper = new QueryWrapper<User>().in("id",ids);

        //3.调用自定义sql方法,该方法在userMapper中声明，在UserMapper中实现
        userMapper.updateBalanceByIds(wrapper,amount);
    }
}