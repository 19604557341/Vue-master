package rouyi.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Integral {
    private Integer id;
    private Integer userId;
    private LocalDateTime tradingTime;
    private LocalDateTime startTime;
    private Integer integral;
}
