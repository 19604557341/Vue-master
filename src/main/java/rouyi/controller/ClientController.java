package rouyi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import rouyi.common.JwtUtilController;
import rouyi.common.R;
import rouyi.dto.ClientDto;
import rouyi.entity.Integral;
import rouyi.entity.Client;
import rouyi.service.IntegralService;
import rouyi.service.ClientService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/user")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private IntegralService integralService;

    /**
     * 客户登录
     * @param request
     * @param client
     * @return
     */
    @PostMapping("/login")
    public R<String> login(HttpServletRequest request, Client client) {
        JwtUtilController jwtUtilController = new JwtUtilController();

        String password = client.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Client> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Client::getPhone, client.getPhone());
        Client u = clientService.getOne(queryWrapper);

        if (u == null) {
            return R.error("当前手机号未注册");
        }

        if (!u.getPassword().equals(password)) {
            return  R.error("密码错误");
        }

        if (u.getStatus() == 0) {
            return  R.error("该账户已禁用");
        }

        String jwtToken = jwtUtilController.generateJwtToken(client.getClientId());
        return R.success(jwtToken);
    }

    /**
     * 员工分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        Page pageInfo = new Page<>(page, pageSize);
        Page<ClientDto> userDtoPage = new Page<>();

        LambdaQueryWrapper<Client> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Client::getUserName, name);
        queryWrapper.orderByDesc(Client::getIntegral);

        clientService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id查询用户信息
     * @param clientId
     * @return
     */
    @GetMapping()
    public R<Client>getById(Integer clientId) {
        Client client = clientService.getById(clientId);
        if (client != null) {
            return R.success(client);
        }
        return R.error("没有查询到对应的员工信息");
    }

    /**
     *  注册客户
     * @param request
     * @param client
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, Client client) {
        log.info(client.toString());

        LambdaQueryWrapper<Client> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Client::getPhone, client.getPhone());
        Client phone = clientService.getOne(queryWrapper);

        if (phone != null) {
            return R.error("当前手机号已注册");
        }

        client.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        client.setStatus(1);
        client.setIntegral(0);
        clientService.save(client);

        return R.success("新增成功");
    }

    /**
     * 根据id修改客户信息
     * @param request
     * @param client
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, Client client) {
        log.info(client.toString());

        LambdaQueryWrapper<Client> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Client::getClientId, client.getClientId());
        Client client1 = clientService.getOne(queryWrapper);
        client.setIntegral(client.getIntegral() + client1.getIntegral());
        if (client.getPassword() != null) {
            client.setPassword(DigestUtils.md5DigestAsHex(client.getPassword().getBytes()));
            BeanUtils.copyProperties(client1, client, "status");
        }else {
            BeanUtils.copyProperties(client1, client);
        }

        clientService.updateById(client);

        return R.success("修改成功");
    }
}
