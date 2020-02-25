package com.fh.common.utils;

import com.fh.common.vo.ResultVo;

public class ResultVoUtil {
    public static ResultVo success(Object object){
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(200);
        resultVo.setMsg("成功");
        resultVo.setData(object);
        return resultVo;
    }

    public static ResultVo success(){
        return success(null);
    }

    public static ResultVo error(Integer code ,String msg){
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(code);
        resultVo.setMsg(msg);
        return resultVo;
    }
}
