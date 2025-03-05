package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service

//这里ServiceImpl实现了IService中的方法
//为什么要指定两个泛型：UserMapper, User呢？
//ServiceImpl实现增删改查不是重写一遍，是去调用BaseMapper，所以需要你手动告诉他是哪个mapper，
public class UserServiceimpl extends ServiceImpl<UserMapper, User> implements IUserService {


   //这里本身就是service，可以直接调用方法
    @Override
    public void deductBalance(Long id, Integer money) {
        //1.查询用户,直接调用
        User user = getById(id);

        //2.校验用户状态
        if (user == null || user.getStatus() == 2){
            throw new RuntimeException("用户状态异常");
        }
        //3.校验用户余额是否充足
        if (user.getBalance() < money){
            throw new RuntimeException("用户余额不足");
        }
        //4.扣减余额update user set balance = balance - ?
        //上述sql语句不建议用MP写，因为会出现在业务层写sql的情况，所以在自定义的sql语句中写，即mapper
        //baseMapper中没有实现扣减余额的方法，因此要自己写
        baseMapper.deductBalance(id, money);


    }

    @Override
    public List<User> querylamda(String name, Integer status, Integer minBalance, Integer maxBalance) {

        //LambdaQuery()
                //.like(User::getUsername,name)
                //.eq(User::getStatus,status)

        return null;
    }

    @Override
    public UserVO queryUserAndAddressById(Long id) {
        //1.查询用户
        User user = getById(id);
        if (user == null || user.getStatus() == 2){
            throw new RuntimeException("用户异常");
        }

        //2.查询地址
        List<Address> address = Db.lambdaQuery(Address.class).eq(Address::getUserId, id).list();

        //3.封装VO
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        if (address != null){
            userVO.setAddresses(BeanUtil.copyToList(address, AddressVO.class));
        }
        return userVO;
    }

    @Override
    public PageDTO<UserVO> pageQueryUsers(UserQuery userQuery) {

        String name = userQuery.getName();
        Integer status = userQuery.getStatus();
//        //1.条件
//            //1.1分页条件
//            Page<User> page = Page.of(userQuery.getPageNo(), userQuery.getPageSize());
//            //1.2排序条件
//            if (userQuery.getSortBy() != null) {
//                page.addOrder(new OrderItem(userQuery.getSortBy(), userQuery.getIsAsc()));
//             }else {
//                page.addOrder(new OrderItem("更新时间", false));
//          }
            //直接调用
        Page<User> page1 = userQuery.toMpPageDefaultSortByUpdateTimeDesc();




        //2.分页查询，这里直接进行查询并分页了,并拿到结果
        Page<User> p = lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != 2, User::getStatus, status)
                .page(page1);

//        //3.封装VO结果
//        PageDTO<UserVO> dto = new PageDTO<>();
//        //3.1总页数
//        dto.setPages(p.getPages());
//        //3.2总条数
//        dto.setTotal(p.getTotal());
//        //3.3当前页数据
//        List<User> records = p.getRecords();
//        if (records == null){
//            dto.setList(Collections.emptyList());
//            return dto;
//        }
//        //3.4内容非空就可以拷贝了
//        List<UserVO> vos = BeanUtil.copyToList(records, UserVO.class);
//        dto.setList(vos);
//        //4.返回
//        return dto;

        //直接调用
        return PageDTO.of(p,user -> {
            //1.拷贝基础属性
            UserVO vo = BeanUtil.copyProperties(user , UserVO.class);
            //2.处理特殊逻辑

            return vo;
        });
    }
}
