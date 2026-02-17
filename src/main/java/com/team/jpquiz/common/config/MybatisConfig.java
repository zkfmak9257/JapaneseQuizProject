package com.team.jpquiz.common.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.team.jpquiz", annotationClass = Mapper.class)
public class MybatisConfig {

}
