package com.lhl.playsdk;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Test {
//    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/api/front/user/login_google")
    Call<LoginModel> appEnter(@Body ReqAppEnter enter);

    class ReqAppEnter {
        String channel = "google";
        String device_id = "837db777-330a-49b6-9a74-b5e89ebd93c68888dadada";
        String id_token = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImNhYWJmNjkwODE5MTYxNmE5MDhhMTM4OTIyMGE5NzViM2MwZmJjYTEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI4Njg3MDYzODczMzMtdTh2Zmp0dm9zaTJyZHM5NzVjcjg1cGo1NnVrNHRva2IuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI4Njg3MDYzODczMzMta2lobDFmc2ZyOGM4aGxpMzhkMTh2cGtqZ3Y5b3RpaDAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDQ4MjYzMTM0MTQ3OTU1Mjg0MTgiLCJlbWFpbCI6ImFiZHVsd2FxYXNraGFuQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiU3Vydml2YWwgZm9yY2VzIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FJdGJ2bW1yVlRGTDIzbXlwUERSckxLNkktOHREaFpxNWdrYkNIZUZ6Ui1pWFE9czk2LWMiLCJnaXZlbl9uYW1lIjoiU3Vydml2YWwiLCJmYW1pbHlfbmFtZSI6ImZvcmNlcyIsImxvY2FsZSI6ImVuIiwiaWF0IjoxNjYzMTM1MDU2LCJleHAiOjE2NjMxMzg2NTZ9.p6YrZbW2BFwHdhjjX4E25Qa-AQYmUZAtKtPxzziy53DHn-tJxhKPwz5KJVg7j80RqFeqJm0qOKU8T6QT9LtdhpUU29AufPfopaD_oxCRAqOOFedwdIsjs539gkrG-Bg5V26jBG0hAYV_mSBr84St8lOonTOCto090XIYZY29zVbuYntJtX6gJ2LtD0bpZh_AETubSn1XRQ2z8Srci3P_YlcwQMjHt_DzlpRiNuPtFA35ZZRCCtqAb9116ciKXinmEzja8586Rri3BQaQLgUH0_akiZe-J69EBGtnKRMqqWnLuZ0MDj7_o2Gwj6rqI14FqT5t7ODz7adOgHKiYg25rw";
        String app_id = "mobybox";
        String os = "android_1.0.0";
    }
    class LoginModel{
        boolean is_new = false;
        String token = null;
        long token_expire =0;
        long uid =0;

        @Override
        public String toString() {
            return "LoginModel{" +
                    "is_new=" + is_new +
                    ", token='" + token + '\'' +
                    ", token_expire=" + token_expire +
                    ", uid=" + uid +
                    '}';
        }
    }
}
