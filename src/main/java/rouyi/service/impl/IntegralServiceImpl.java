package rouyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import rouyi.entity.Integral;
import rouyi.mapper.IntegralMapper;
import rouyi.service.IntegralService;

@Service
public class IntegralServiceImpl extends ServiceImpl<IntegralMapper, Integral> implements IntegralService {
}
