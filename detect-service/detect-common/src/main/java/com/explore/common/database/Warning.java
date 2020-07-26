package com.explore.common.database;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author PinTeh
 * @date 2020/7/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Warning {

    private Integer id;

    private Integer number;

    private Integer warning;

    private LocalDateTime createTime;
}
