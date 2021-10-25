package top.maof.haikang.service;

import top.maof.haikang.model.User;

public interface UserService {
    User get(Integer id);

    User getWithRole(Integer id);

    User getWithRolePerm(Integer id);

    User selectByUsername(String username);

    int updatePwd(int id,String password);
}
