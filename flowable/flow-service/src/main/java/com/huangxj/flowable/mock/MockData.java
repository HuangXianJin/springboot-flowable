package com.huangxj.flowable.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author huangxj
 * @version 1.0
 * @date 2022/4/7 11:46
 */
public class MockData {

    static  ArrayList arrayList = new ArrayList();

    static {
        arrayList.add(new User("admin", "管理员"));
        arrayList.add(new User("zhangsan", "张三"));
        arrayList.add(new User("jingli", "经理"));
        arrayList.add(new User("zhuguan", "主管"));

    }

    public static List<User> getUserList() {
        return arrayList;

    }
    public static User getCurrentUser() {
      return  new User("admin", "管理员");
    }

    public class UserInfo {


    }
}
