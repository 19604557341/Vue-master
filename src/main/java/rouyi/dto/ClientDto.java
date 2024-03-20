package rouyi.dto;

import lombok.Data;
import rouyi.entity.Client;

@Data
public class ClientDto extends Client {
    private Integer integral;
}
