package com.itheima.mp.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
class IUserServiceTest {

    @Autowired
    private IUserService userService;


    @Test
    void testInsert() {
        User user = new User();

        user.setUsername("zhangdapao3");
        user.setPassword("1234");
        user.setPhone("18688990015");
        user.setBalance(2000);
        user.setInfo("{\"age\": 24, \"intro\": \"老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userService.save(user);
    }


    //分页查询测试
    @Test
    void testPageQuery(){
        int pageno = 1,pagesize = 2;

        //1.1分页条件
        Page<User> page = Page.of(pageno, pagesize);
        //1.2排序条件
        page.addOrder(new OrderItem("balance",true));
        page.addOrder(new OrderItem("id",true));

        //2.分页
        Page<User> p = userService.page(page);

        //3.解析
        long total = p.getTotal();
        System.out.println("totall = "+total);

        List<User> records = p.getRecords();
        records.forEach(System.out::println);

        long pages = p.getPages();
        System.out.println("pages = "+pages);


    }

}