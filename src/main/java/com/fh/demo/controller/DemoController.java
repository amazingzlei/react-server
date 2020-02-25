package com.fh.demo.controller;

import com.fh.common.utils.ResultVoUtil;
import com.fh.common.vo.ResultVo;
import com.fh.demo.vo.CarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DemoController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/getCar")
    @ResponseBody
    public ResultVo test(){
        String sql = "select * from car";
        RowMapper rowMapper = new BeanPropertyRowMapper(CarVo.class);
        List<CarVo> carVos = jdbcTemplate.query(sql, rowMapper);
        return ResultVoUtil.success(carVos);
    }

    @RequestMapping("/getComments")
    @ResponseBody
    public ResultVo getComments(){
        String sql = "select * from car";
        RowMapper rowMapper = new BeanPropertyRowMapper(CarVo.class);
        List<CarVo> carVos = jdbcTemplate.query(sql, rowMapper);
        return ResultVoUtil.success(carVos);
    }
}
