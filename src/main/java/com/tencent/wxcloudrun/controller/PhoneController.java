package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phone")
public class PhoneController {

    @PostMapping
    public ApiResponse submitPhone(@RequestBody PhoneRequest request) {
        String phone = request.getPhone();
        // 这里可以添加手机号验证和处理逻辑
        System.out.println("Received phone: " + phone);
        // 模拟处理成功
        return ApiResponse.ok("手机号提交成功");
    }

    static class PhoneRequest {
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
