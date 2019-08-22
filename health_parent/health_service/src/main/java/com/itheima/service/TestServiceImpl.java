package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.itheima.entity.Result;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
@Service(interfaceClass = TestService.class)
@Transactional
public class TestServiceImpl implements TestService {
    @Override
    public String test() {

        Result a = Result.error("xx");
        return "hi fzoo";
    }

}