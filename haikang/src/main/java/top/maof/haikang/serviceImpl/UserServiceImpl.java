package top.maof.haikang.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.maof.haikang.mapper.UserMapper;
import top.maof.haikang.model.User;
import top.maof.haikang.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User get(Integer id) {
        return userMapper.get(id);
    }

    @Override
    public User getWithRole(Integer id) {
        return userMapper.getWithRole(id);
    }

    @Override
    public User getWithRolePerm(Integer id) {
        return userMapper.getWithRolePerm(id);
    }

    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public int updatePwd(int id,String password) {
        return userMapper.updatePwd(id,password);
    }
}
