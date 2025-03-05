package com.itheima.mp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "用户管理接口")
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

   private final IUserService userService;

   @ApiOperation("新增用户接口")
   @PostMapping
   public void saveUser(@RequestBody UserFormDTO userFormDTO){
        //1.把DTO拷贝到PO
        //java11的写法
        //User user = BeanUtils.copyProperties(userFormDTO, User.class);
        User user = new User();
        BeanUtils.copyProperties(userFormDTO, user);
        //2.新增
        userService.save(user);
   }

    @ApiOperation("删除用户接口")
    @DeleteMapping("{id}")
    public void deleteUser(@ApiParam("用户id") @PathVariable("id") Long id){
        userService.removeById(id);
    }


    @ApiOperation("查询用户接口")
    @GetMapping("{id}")
    public UserVO queryUser(@ApiParam("用户id") @PathVariable("id") Long id){
        return userService.queryUserAndAddressById(id);
    }

    @ApiOperation("根据id批量查询用户接口")
    @GetMapping
    public List<UserVO> queryUserbylist(@ApiParam("用户id集合") @RequestParam("ids") List<Long> ids){
        // 获取用户列表
        List<User> users = userService.listByIds(ids);
        // 创建UserVO列表用于返回
        List<UserVO> userVOList = new ArrayList<>();
        // 将每个User对象转换成UserVO对象
        for (User user : users) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);  // 将User属性复制到UserVO
            userVOList.add(userVO);
        }
        return userVOList;  // 返回转换后的UserVO列表
    }



    //上述业务比较简单，可以直接使用Service中的方法
    //下面的业务会更复杂，需要自己写需求


    @ApiOperation("扣减用户余额接口")
    @DeleteMapping("{id}/deduction/{money}")
    public void deductMoneyById(
            @ApiParam("用户id") @PathVariable("id") Long id,
            @ApiParam("扣减的金额") @PathVariable("money") Integer money){

        userService.deductBalance(id,money);
    }


    //未完成
    @ApiOperation("根据复杂条件查询用户接口")
    @GetMapping("/list")
    public List<User> queryusers(UserQuery userQuery){
       List<User> users = userService.querylamda(userQuery.getName(),userQuery.getStatus(),userQuery.getMinBalance(),userQuery.getMaxBalance());
       return users;
    }

    @ApiOperation("根据条件分页查询用户接口")
    @GetMapping("/page")
    public PageDTO<UserVO> pageQueryUsers(UserQuery userQuery){
        return userService.pageQueryUsers(userQuery);
    }


    //返回男性用户
    @ApiOperation("查询用户接口")
    @GetMapping("{id}")
    public UserVO queryUserman(@ApiParam("用户id") @PathVariable("id") Long id){
        return userService.queryUserAndAddressById(id);
    }
}
